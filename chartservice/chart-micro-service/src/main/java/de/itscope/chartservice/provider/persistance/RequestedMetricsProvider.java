package de.itscope.chartservice.provider.persistance;

import com.google.common.base.Stopwatch;
import de.itscope.chartservice.metrics.FunnelsEvent;
import de.itscope.chartservice.metrics.RetentionEvent;
import de.itscope.chartservice.metrics.SegmentationEvent;
import de.itscope.chartservice.model.repository.FunnelsRepository;
import de.itscope.chartservice.model.repository.RetentionRepository;
import de.itscope.chartservice.model.repository.SegmentationRepository;
import de.itscope.chartservice.model.storage.Funnels;
import de.itscope.chartservice.model.storage.Retention;
import de.itscope.chartservice.model.storage.Segmentation;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi.MPFunnelsApi;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.retentionApi.MPRetentionApiRedgiant;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.segmentationApi.Data;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.segmentationApi.MPSegmentationApi;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.segmentationApi.Values;
import de.itscope.chartservice.util.funnelRangeUtils.FunnelRange;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class RequestedMetricsProvider {
    public MPSegmentationApi getSegmentationRecource(SegmentationRepository repository, String event, String domain, String breakdownType, ArrayList<String> dateStringsForRange, SegmentationEvent requestMetricsEvent) {
        List<Segmentation> requestedData;
        Stopwatch recieveDataTime = Stopwatch.createStarted();
        requestedData = repository.findByEventAndDomainAndBreakdownTypeAndDateIn(event, domain, breakdownType, dateStringsForRange);
        recieveDataTime.stop();
        requestMetricsEvent.setRetrieveRequestedDataDbCallTime(recieveDataTime.elapsed(TimeUnit.MILLISECONDS));

        //prepare data from cache for the rest reply
        Stopwatch prepareDataTime = Stopwatch.createStarted();

        Map<String, Map<String, Integer>> additionalProperties = new HashMap<>();
        int[] countValues = new int[]{0};
        requestedData.stream().filter(s -> s.getBreakdownValue() != null || !s.getBreakdownValue().isEmpty()).collect(groupingBy(Segmentation::getBreakdownValue)).entrySet().forEach(breakdownEntrySet -> {
            Map<String, Integer> dateValuePairs = new HashMap<>();
            dateStringsForRange.forEach(d -> {
                dateValuePairs.put(d, 0);
            });
            breakdownEntrySet.getValue().forEach(segmentation -> {
                countValues[0]++;
                dateValuePairs.put(segmentation.getDate(), segmentation.getValue());
            });
            additionalProperties.put(breakdownEntrySet.getKey(), dateValuePairs);
        });

        requestMetricsEvent.setAmountOfDataProvided(countValues[0]);

        Data dataSingleDomain = new Data();
        dataSingleDomain.setValues(new Values());
        dataSingleDomain.getValues().getAdditionalProperties().putAll(additionalProperties);
        MPSegmentationApi mpSegmentationApi = new MPSegmentationApi();
        mpSegmentationApi.setData(dataSingleDomain);

        prepareDataTime.stop();
        requestMetricsEvent.setPrepareRequestedDataTime(prepareDataTime.elapsed(TimeUnit.MILLISECONDS));

        return mpSegmentationApi;
    }

    public MPRetentionApiRedgiant getRetentionRecource(RetentionRepository repository, String born_event, String event, String where, ArrayList<String> dateStringsForRange, RetentionEvent retentionEvent) {
        List<Retention> requestedData;
        Stopwatch recieveDataTime = Stopwatch.createStarted();
        requestedData = repository.findByBornEventAndEventAndDomainAndDateIn(born_event, event, where, dateStringsForRange)
                .stream().sorted(Comparator.comparing(Retention::getDate)).collect(Collectors.toList());
        recieveDataTime.stop();
        retentionEvent.setRetrieveRequestedDataDbCallTime(recieveDataTime.elapsed(TimeUnit.MILLISECONDS));

        //prepare data from cache for the rest reply
        Stopwatch prepareDataTime = Stopwatch.createStarted();
        int[] countValues = new int[]{0};

        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Integer> first = new ArrayList<>();
        List<List<Integer>> counts = new ArrayList<>();
        requestedData.stream().forEach(s -> {
            countValues[0]++;
            dates.add(s.getDate());
            first.add(s.getFirst());
            counts.add(s.getCounts());
        });

        retentionEvent.setAmountOfDataProvided(countValues[0]);

        int[] firstArr = first.stream().mapToInt(i -> i).toArray();

        int[][] countsArr = counts.stream().map(l -> l.stream().mapToInt(i -> i).toArray()).collect(Collectors.toList()).toArray(new int[requestedData.size()][]);
        MPRetentionApiRedgiant retentionApiRedgiant = new MPRetentionApiRedgiant(countsArr, born_event, event, dates.toArray(String[]::new), firstArr);

        prepareDataTime.stop();
        retentionEvent.setPrepareRequestedDataTime(prepareDataTime.elapsed(TimeUnit.MILLISECONDS));

        return retentionApiRedgiant;
    }

    public MPFunnelsApi getFunnelsRecource(FunnelsRepository repository, int funnelsID, String domain, FunnelRange dateRange, FunnelsEvent requestMetricsEvent) {
        Stopwatch recieveDataTime = Stopwatch.createStarted();
        List<Funnels> requestedData = repository.findByFunnelIDAndDomainAndFunnelRange(funnelsID, domain, dateRange.name());

        recieveDataTime.stop();
        requestMetricsEvent.setRetrieveRequestedDataDbCallTime(recieveDataTime.elapsed(TimeUnit.MILLISECONDS));

        Stopwatch prepareDataTime = Stopwatch.createStarted();

        Map<String, List<Funnels>> steps = requestedData.stream().collect(groupingBy(Funnels::getStepLabel));
        Map<String, Integer> stepsWithValues = steps.entrySet().stream().collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue().stream().map(Funnels::getCount).reduce(Integer::sum).get()));
        de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi.Data dataSingleDomain = new de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi.Data();

        requestMetricsEvent.setAmountOfDataProvided(stepsWithValues.size());

        dataSingleDomain.getAdditionalProperties().putAll(stepsWithValues);
        MPFunnelsApi mpFunnelsApi = new MPFunnelsApi();
        mpFunnelsApi.setData(dataSingleDomain);
        prepareDataTime.stop();
        requestMetricsEvent.setPrepareRequestedDataTime(prepareDataTime.elapsed(TimeUnit.MILLISECONDS));
        return mpFunnelsApi;
    }
}
