package de.itscope.chartservice.provider.persistance;

import com.google.common.base.Stopwatch;
import de.itscope.chartservice.metrics.*;
import de.itscope.chartservice.model.repository.FunnelsRepository;
import de.itscope.chartservice.model.repository.ProviderTaskRepository;
import de.itscope.chartservice.model.repository.RetentionRepository;
import de.itscope.chartservice.model.repository.SegmentationRepository;
import de.itscope.chartservice.model.storage.Funnels;
import de.itscope.chartservice.model.storage.ProviderTask;
import de.itscope.chartservice.model.storage.Retention;
import de.itscope.chartservice.model.storage.Segmentation;
import de.itscope.chartservice.provider.rest.DataProvider;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi.MPFunnelsApi;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.retentionApi.MPRetentionApi;
import de.itscope.chartservice.provider.rest.urlUtil.apiUriComposer.mixpanelApiProvider.FunnelsApiProvider;
import de.itscope.chartservice.provider.rest.urlUtil.apiUriComposer.mixpanelApiProvider.JqlSegmentationApiProvider;
import de.itscope.chartservice.provider.rest.urlUtil.apiUriComposer.mixpanelApiProvider.RetentionApiProvider;
import de.itscope.chartservice.util.DateStringUtils;
import net.monofraps.influxmetrics.MetricTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
@Profile("updateCache")
public class ScheduledTaskRunner {
    private static final Log log = LogFactory.getLog(ScheduledTaskRunner.class);
    private static final int iterationsPerHour = 3;
    private static final int jqlLimit = 60;
    private static final int formattedLimit = 400;
    private static final int maximumTasksPerBatch = 10;
    private static int currentIteration = 0;
    private static int jqlCount = 0;
    private static int formattedCount = 0;
    @Autowired
    ProviderTaskRepository taskRepository;
    @Autowired
    SegmentationRepository segmentationRepository;
    @Autowired
    FunnelsRepository funnelsRepository;
    @Autowired
    RetentionRepository retentionRepository;
    @Value("${itscope.mixpanel.b2b.apisecret}")
    private String mpSecret;
    @Autowired
    private MetricsSenderService metricsSenderService;

    public void forceTask(ProviderTask currentTask, ManualUpdateEvent requestMetricsEvent) {
        Stopwatch updateCacheTime = Stopwatch.createStarted();
        completeTask(currentTask);
        updateCacheTime.stop();
        requestMetricsEvent.setCompleteUpdateCacheTime(updateCacheTime.elapsed(TimeUnit.MILLISECONDS));
    }

    @Scheduled(fixedRate = 900000)
    public void runTasks() {
        UpdateCacheEvent requestMetricsEvent = new UpdateCacheEvent("scheduledUpdate");
        ArrayList<MetricTag> tags = new ArrayList<>();
        tags.add(new MetricTag("scope", requestMetricsEvent.getScope()));
        Stopwatch updateCacheTime = Stopwatch.createStarted();
        log.info("Updating local cache...");
        if (currentIteration > iterationsPerHour) {
            currentIteration = 0;
            jqlCount = 0;
            formattedCount = 0;
        }
        currentIteration++;
        Stopwatch findTasksTime = Stopwatch.createStarted();

        Page<ProviderTask> tasks = taskRepository.findAll(
                PageRequest.of(0, maximumTasksPerBatch, Sort.by(Sort.Direction.DESC, "priority")));

        findTasksTime.stop();
        requestMetricsEvent.setFindUpdateCacheTasksTime(findTasksTime.elapsed(TimeUnit.MILLISECONDS));

        int[] logCounter = {0};

        tasks.get().filter(t -> t.getPriority() > 0).forEach(currentTask -> {
            completeTask(currentTask);
            currentTask.resetPriority();
            taskRepository.save(currentTask);
            logCounter[0]++;
        });

        log.info("" + ScheduledTaskRunner.class + " completed updating local cache with " + logCounter[0] + " requests send.");
        updateCacheTime.stop();
        requestMetricsEvent.setCompleteUpdateCacheTime(updateCacheTime.elapsed(TimeUnit.MILLISECONDS));
        requestMetricsEvent.setTasksInBatch(logCounter[0]);
        LogMetricsUtils.getUpdateCacheEventSeries(metricsSenderService,requestMetricsEvent.getScope(), tags).commitEvent(requestMetricsEvent);
    }

    private void completeTask(ProviderTask currentTask) {
        ArrayList<MetricTag> tags = new ArrayList<>();
        SingleTaskEvent event = new SingleTaskEvent("singleTask");
        tags.add(new MetricTag("scope", event.getScope()));

        event.setSingleTaskSerial(String.valueOf(currentTask.getSerial()));
        tags.add(new MetricTag("serial", event.getSingleTaskSerial()));
        log.info("starting with task " + currentTask.toString() + ". Current priority: " + currentTask.getPriority());

        switch (currentTask.getType()) {
            case Funnels -> {
                //JQL Api requests also count for the formatted rate limit
                if (jqlCount > jqlLimit || formattedCount > formattedLimit) {
                    log.info("" + ScheduledTaskRunner.class +
                            " could not update local cache: Mixpanels Api's rate limit is used up. Task " +
                            currentTask.toString() + "gets skipped.");

                    return;
                }
                jqlCount++;
                formattedCount++;

                event.setDateRangeInDays((int)currentTask.getFunnelRange().getDateRange());
                tags.add(new MetricTag("TaskType", currentTask.getType().name()));

                long dateRange = currentTask.getFunnelRange().getDateRange();
                String fromDate = DateStringUtils.daysAgo((int) dateRange);
                FunnelsApiProvider provider = new FunnelsApiProvider(currentTask.getFunnelID(), fromDate, DateStringUtils.today(), currentTask.getBreakdown());
                Stopwatch requestCacheableDataTime = Stopwatch.createStarted();
                MPFunnelsApi modelAllDomains;
                try {
                    modelAllDomains= (new DataProvider(mpSecret)).provide(provider.compose(), FunnelsApiProvider.returnType);
                } catch (HttpClientErrorException e) {
                    e.printStackTrace();
                    return;
                }

                requestCacheableDataTime.stop();
                event.setRequestCacheableDataTime(requestCacheableDataTime.elapsed(TimeUnit.MILLISECONDS));

                int[] totalValueCounter = new int[]{0};
                int[] updatedValueCounter = new int[]{0};
                Stopwatch updateDbTime = Stopwatch.createStarted();

                modelAllDomains.getData().getAdditionalProperties().entrySet().forEach(d -> {
                    Map.Entry<String, Map> dateEntry = (Map.Entry) d;
                    dateEntry.getValue().entrySet().forEach(e -> {
                        Map.Entry<String, List> domainEntry = (Map.Entry) e;
                        domainEntry.getValue().forEach(f -> {
                            totalValueCounter[0]++;
                            Map<String, Object> eventEntry = (Map) f;
                            Funnels funnels = new Funnels(currentTask.getFunnelID(), domainEntry.getKey(), (String) eventEntry.get("step_label"), currentTask.getFunnelRange(), (int) eventEntry.get("count"));
                            if (!funnelsRepository.existsByFunnelIDAndDomainAndStepLabelAndFunnelRange(funnels.getFunnelID(), funnels.getDomain(), funnels.getStepLabel(), currentTask.getFunnelRange().name())) {
                                updatedValueCounter[0]++;
                                funnelsRepository.save(funnels);
                            } else if (!funnelsRepository.existsByFunnelIDAndDomainAndStepLabelAndFunnelRangeAndCount(funnels.getFunnelID(), funnels.getDomain(), funnels.getStepLabel(), currentTask.getFunnelRange().name(), funnels.getCount())) {
                                updatedValueCounter[0]++;
                                Funnels currentDatum = funnelsRepository.findByFunnelIDAndDomainAndStepLabelAndFunnelRange(funnels.getFunnelID(), funnels.getDomain(), funnels.getStepLabel(), currentTask.getFunnelRange().name());
                                currentDatum.setCount(funnels.getCount());
                                funnelsRepository.save(currentDatum);
                            }
                        });
                    });
                });

                event.setAmountOfDataRequested(totalValueCounter[0]);
                event.setAmountOfRequestedDataUsed(updatedValueCounter[0]);
                log.info("Task " + currentTask.toString() + " completed successfully.");
                updateDbTime.stop();
                event.setUpdateDbTableEntryTime(updateDbTime.elapsed(TimeUnit.MILLISECONDS));
                LogMetricsUtils.getSingleTaskEventSeries(metricsSenderService,event.getScope(),tags).commitEvent(event);
            }
            case Segmentation -> {
                if (formattedCount > formattedLimit) {
                    log.info("" + ScheduledTaskRunner.class +
                            " could not update local cache: Mixpanels Api's rate limit is used up. Task " +
                            currentTask.toString() + "gets skipped.");
                    return;
                }
                formattedCount++;

                event.setDateRangeInDays(DateStringUtils.getDateStringsForRange(currentTask.getFrom_Date(), currentTask.getTo_Date()).size());
                tags.add(new MetricTag("TaskType", currentTask.getType().name()));

                ArrayList<String> groupByProperties = new ArrayList<>();
                groupByProperties.add(currentTask.getSpecifier());
                if (!currentTask.getBreakdown().isEmpty()) {
                    groupByProperties.add(currentTask.getBreakdown());
                }

                //get data from mixpanel
                JqlSegmentationApiProvider jqlProvider = new JqlSegmentationApiProvider(currentTask.getEvent(), currentTask.getFrom_Date(), currentTask.getTo_Date(), groupByProperties.toArray(new String[0]));
                Stopwatch requestCacheableDataTime = Stopwatch.createStarted();
                ArrayList<Map<String, Object>> provide = (new DataProvider(mpSecret)).provide(jqlProvider.compose(), JqlSegmentationApiProvider.returnType);
                requestCacheableDataTime.stop();
                event.setRequestCacheableDataTime(requestCacheableDataTime.elapsed(TimeUnit.MILLISECONDS));

                Stopwatch updateDbTime = Stopwatch.createStarted();
                String finalShortenedOn = currentTask.getBreakdown();
                int[] totalValueCounter = new int[]{0};
                int[] updatedValueCounter = new int[]{0};

                provide.stream()
                        .forEach(s -> {
                            totalValueCounter[0]++;
                            ArrayList key = (ArrayList) s.get("key");
                            int value = (int) s.get("value");
                            Segmentation segmentation;
                            if (key.size() < 3) {
                                segmentation = new Segmentation(currentTask.getEvent(), (String) key.get(1), finalShortenedOn, "", DateStringUtils.getStringFromDate(new Date((long) key.get(0))), value);
                            } else {
                                segmentation = new Segmentation(currentTask.getEvent(), (String) key.get(1), finalShortenedOn, (String) key.get(2), DateStringUtils.getStringFromDate(new Date((long) key.get(0))), value);
                            }
                            if (!segmentationRepository.existsByEventAndDomainAndBreakdownTypeAndBreakdownValueAndDate(segmentation.getEvent(), segmentation.getDomain(), segmentation.getBreakdownType(), segmentation.getBreakdownValue(), segmentation.getDate())) {
                                updatedValueCounter[0]++;
                                segmentationRepository.save(segmentation);
                            } else if (!segmentationRepository.existsByEventAndDomainAndBreakdownTypeAndBreakdownValueAndDateAndValue(segmentation.getEvent(), segmentation.getDomain(), segmentation.getBreakdownType(), segmentation.getBreakdownValue(), segmentation.getDate(), segmentation.getValue())) {
                                updatedValueCounter[0]++;
                                Segmentation currentDatum = segmentationRepository.findByEventAndDomainAndBreakdownTypeAndBreakdownValueAndDate(segmentation.getEvent(), segmentation.getDomain(), segmentation.getBreakdownType(), segmentation.getBreakdownValue(), segmentation.getDate());
                                currentDatum.setValue(segmentation.getValue());
                                segmentationRepository.save(currentDatum);
                            }
                        });
                event.setAmountOfDataRequested(totalValueCounter[0]);
                event.setAmountOfRequestedDataUsed(updatedValueCounter[0]);
                log.info("Task " + currentTask.toString() + " completed successfully.");
                updateDbTime.stop();
                event.setUpdateDbTableEntryTime(updateDbTime.elapsed(TimeUnit.MILLISECONDS));
                LogMetricsUtils.getSingleTaskEventSeries(metricsSenderService,event.getScope(), tags).commitEvent(event);
            }
            case Retention -> {
                if (formattedCount > formattedLimit) {
                    log.info("" + ScheduledTaskRunner.class +
                            " could not update local cache: Mixpanels Api's rate limit is used up. Task " +
                            currentTask.toString() + "gets skipped.");
                    return;
                }
                formattedCount++;

                event.setDateRangeInDays(DateStringUtils.getDateStringsForRange(currentTask.getFrom_Date(), currentTask.getTo_Date()).size());
                tags.add(new MetricTag("TaskType", currentTask.getType().name()));

                //get data from mixpanel
                RetentionApiProvider provider = new RetentionApiProvider(currentTask.getFrom_Date(), currentTask.getTo_Date(), currentTask.getBornEvent(), currentTask.getEvent(), currentTask.getSpecifier());
                Stopwatch requestCacheableDataTime = Stopwatch.createStarted();
                MPRetentionApi provide = (new DataProvider(mpSecret)).provide(provider.compose(), RetentionApiProvider.returnType);
                requestCacheableDataTime.stop();
                event.setRequestCacheableDataTime(requestCacheableDataTime.elapsed(TimeUnit.MILLISECONDS));

                Stopwatch updateDbTime = Stopwatch.createStarted();
                int[] totalValueCounter = new int[]{0};
                int[] updatedValueCounter = new int[]{0};

                provide.getDateToDomain().forEach((date,domains) -> {
                    domains.getDomainsToValue().forEach((domain, values) -> {
                        totalValueCounter[0]++;
                        Retention retention = new Retention(currentTask.getBornEvent(), currentTask.getEvent(), domain, date, values.getFirst(), values.getCounts());

                        if (!retentionRepository.existsByBornEventAndEventAndDomainAndDate(currentTask.getBornEvent(), currentTask.getEvent(), domain, date)){
                            updatedValueCounter[0]++;
                            retentionRepository.save(retention);
                        } else if (!retentionRepository.existsByBornEventAndEventAndDomainAndDateAndFirstAndCountsIn(currentTask.getBornEvent(), currentTask.getEvent(), domain, date, values.getFirst(), values.getCounts())){
                            updatedValueCounter[0]++;
                            Retention currentDatum = retentionRepository.findByBornEventAndEventAndDomainAndDate(currentTask.getBornEvent(), currentTask.getEvent(), domain, date);
                            currentDatum.setFirst(retention.getFirst());
                            currentDatum.setCounts(retention.getCounts());
                            retentionRepository.save(currentDatum);
                        }
                    });
                });

                event.setAmountOfDataRequested(totalValueCounter[0]);
                event.setAmountOfRequestedDataUsed(updatedValueCounter[0]);
                log.info("Task " + currentTask.toString() + " completed successfully.");
                updateDbTime.stop();
                event.setUpdateDbTableEntryTime(updateDbTime.elapsed(TimeUnit.MILLISECONDS));
                LogMetricsUtils.getSingleTaskEventSeries(metricsSenderService,event.getScope(), tags).commitEvent(event);
            }
        }
    }
}
