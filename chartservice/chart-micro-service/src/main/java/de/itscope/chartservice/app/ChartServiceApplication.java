package de.itscope.chartservice.app;

import ch.qos.logback.access.jetty.RequestLogImpl;
import com.google.common.base.Stopwatch;
import de.itscope.chartservice.metrics.*;
import de.itscope.chartservice.model.repository.FunnelsRepository;
import de.itscope.chartservice.model.repository.ProviderTaskRepository;
import de.itscope.chartservice.model.repository.RetentionRepository;
import de.itscope.chartservice.model.repository.SegmentationRepository;
import de.itscope.chartservice.model.storage.ProviderTask;
import de.itscope.chartservice.model.storage.Retention;
import de.itscope.chartservice.model.storage.Segmentation;
import de.itscope.chartservice.provider.persistance.ProviderTaskManager;
import de.itscope.chartservice.provider.persistance.RequestedMetricsProvider;
import de.itscope.chartservice.provider.persistance.ScheduledTaskRunner;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi.MPFunnelsApi;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.retentionApi.MPRetentionApiRedgiant;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.segmentationApi.MPSegmentationApi;
import de.itscope.chartservice.util.DateStringUtils;
import de.itscope.chartservice.util.funnelRangeUtils.FunnelRange;
import net.monofraps.influxmetrics.MetricTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.AsyncNCSARequestLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.embedded.JettyWebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.jetty.ConfigurableJettyWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@Configuration
@ComponentScan({"de.itscope.chartservice"})
@EntityScan({"de.itscope.chartservice"})
@EnableAutoConfiguration
@EnableJpaRepositories({"de.itscope.chartservice"})
@EnableScheduling
public class ChartServiceApplication {

    private final static Log log = LogFactory.getLog(ChartServiceApplication.class);
    @Autowired
    private MetricsSenderService metricsSenderService;

    public static void main(String[] args) {
        SpringApplication.run(ChartServiceApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty("itscope.logging.access.enabled")
    public JettyWebServerFactoryCustomizer accessLoggingCustomizer(Environment environment, ServerProperties serverProperties) {
        return new JettyWebServerFactoryCustomizer(environment, serverProperties) {
            @Override
            public void customize(ConfigurableJettyWebServerFactory factory) {
                super.customize(factory);
                factory.addServerCustomizers((JettyServerCustomizer) server -> {
                    if (Arrays.asList(environment.getActiveProfiles()).contains("json-log")) {
                        RequestLogImpl requestLogImpl = new RequestLogImpl();
                        requestLogImpl.setResource("/logback-access.xml");
                        requestLogImpl.start();
                        server.setRequestLog(requestLogImpl);
                    } else {
                        server.setRequestLog(new AsyncNCSARequestLog());
                    }
                });
            }
        };
    }

    @RestController
    @RequestMapping("/update")
    @Profile("updateCache")
    class UpdateCacheApiController {
        @Autowired
        private ScheduledTaskRunner runner;

        @RequestMapping(path = "/mixpanel/segmentation")
        void updateSegmentationCache(@RequestParam String event, @RequestParam String from_date,
                                     @RequestParam String to_date, @RequestParam String where,
                                     @RequestParam(required = false) String on) {
            //performance metrics and logging
            ManualUpdateEvent requestMetricsEvent = new ManualUpdateEvent("_update_mixpanel_segmentation");
            ArrayList<MetricTag> tags = new ArrayList<>();
            tags.add(new MetricTag("scope", requestMetricsEvent.getScope()));
            requestMetricsEvent.setDateRangeInDays(DateStringUtils.getDateStringsForRange(from_date, to_date).size());
            log.info("Restcontroller with mapping '/update/mixpanel/segmentation' received a request");
            Stopwatch overallTime = Stopwatch.createStarted();

            //prepare parameters
            String shortenedWhere = where.replace("properties[", "").replace("]", "").replace("\"", "");
            String wherePropertyShortened = shortenedWhere.substring(0, shortenedWhere.indexOf("=="));
            String shortenedOn = on != null ? on.replace("properties[", "").replace("]", "").replace("\"", "") : "";

            runner.forceTask(new ProviderTask(ProviderTask.ProviderType.Segmentation, event, from_date, to_date, wherePropertyShortened, shortenedOn), requestMetricsEvent);

            //performance metrics and logging
            log.info("Restcontroller with mapping 'update/mixpanel/segmentation' finished request successfully");
            overallTime.stop();
            requestMetricsEvent.setCompleteRequestTime(overallTime.elapsed(TimeUnit.MILLISECONDS));
            LogMetricsUtils.getManualUpdateEventSeries(metricsSenderService, requestMetricsEvent.getScope(), tags).commitEvent(requestMetricsEvent);
        }

        @RequestMapping(path = "/mixpanel/retention")
        void updateRetentionCache(@RequestParam String from_date,
                                  @RequestParam String to_date, @RequestParam String born_event, @RequestParam String event, @RequestParam String where) {
            //performance metrics and logging
            ManualUpdateEvent requestMetricsEvent = new ManualUpdateEvent("_update_mixpanel_retention");
            ArrayList<MetricTag> tags = new ArrayList<>();
            tags.add(new MetricTag("scope", requestMetricsEvent.getScope()));
            requestMetricsEvent.setDateRangeInDays(DateStringUtils.getDateStringsForRange(from_date, to_date).size());
            log.info("Restcontroller with mapping '/update/mixpanel/retention' received a request");
            Stopwatch overallTime = Stopwatch.createStarted();

            //prepare parameters
            String shortenedWhere = where.replace("properties[", "").replace("]", "").replace("\"", "");
            String wherePropertyShortened = shortenedWhere.substring(0, shortenedWhere.indexOf("=="));

            runner.forceTask(new ProviderTask(ProviderTask.ProviderType.Retention, born_event, event, from_date, to_date, 5, wherePropertyShortened), requestMetricsEvent);

            //performance metrics and logging
            log.info("Restcontroller with mapping 'update/mixpanel/retention' finished request successfully");
            overallTime.stop();
            requestMetricsEvent.setCompleteRequestTime(overallTime.elapsed(TimeUnit.MILLISECONDS));
            LogMetricsUtils.getManualUpdateEventSeries(metricsSenderService, requestMetricsEvent.getScope(), tags).commitEvent(requestMetricsEvent);
        }

        @RequestMapping(path = "/mixpanel/funnels")
        void updateFunnelsCache(@RequestParam int funnel_id, @RequestParam FunnelRange funnelsDateRange, @RequestParam String where) {
            //performance metrics and logging
            ManualUpdateEvent requestMetricsEvent = new ManualUpdateEvent("_update_mixpanel_funnels");
            ArrayList<MetricTag> tags = new ArrayList<>();
            tags.add(new MetricTag("scope", requestMetricsEvent.getScope()));
            requestMetricsEvent.setDateRangeInDays(funnelsDateRange.getDateRange());
            log.info("Restcontroller with mapping '/update/mixpanel/funnels' received a request");
            Stopwatch overallTime = Stopwatch.createStarted();

            //prepare parameters
            String shortenedWhere = where.replace("properties[", "").replace("]", "").replace("\"", "");
            String wherePropertyShortened = shortenedWhere.substring(0, shortenedWhere.indexOf("=="));

            runner.forceTask(new ProviderTask(ProviderTask.ProviderType.Funnels, funnel_id, funnelsDateRange, wherePropertyShortened), requestMetricsEvent);

            //performance metrics and logging
            log.info("Restcontroller with mapping 'update/mixpanel/funnels' finished request successfully");
            overallTime.stop();
            requestMetricsEvent.setCompleteRequestTime(overallTime.elapsed(TimeUnit.MILLISECONDS));
            LogMetricsUtils.getManualUpdateEventSeries(metricsSenderService, requestMetricsEvent.getScope(), tags).commitEvent(requestMetricsEvent);
        }
    }

    @RestController
    @RequestMapping("/mixpanel")
    @Profile("provideCache")
    class MixpanelApiController {

        @Autowired
        private SegmentationRepository segmentationRepository;
        @Autowired
        private FunnelsRepository funnelsRepository;
        @Autowired
        private RetentionRepository retentionRepository;
        @Autowired
        private ProviderTaskRepository providerTaskRepository;

        @Value("${itscope.mixpanel.b2b.apisecret}")
        private String mpSecret;

        @RequestMapping(path = "/retention")
        MPRetentionApiRedgiant retention(@RequestParam String from_date,
                                         @RequestParam String to_date, @RequestParam String born_event, @RequestParam String event, @RequestParam String where) {
            //performance metrics and logging
            RetentionEvent retentionEvent = new RetentionEvent("_mixpanel_retention");
            ArrayList<MetricTag> tags = new ArrayList<>();
            tags.add(new MetricTag("scope", retentionEvent.getScope()));
            retentionEvent.setDateRangeInDays(DateStringUtils.getDateStringsForRange(from_date, to_date).size());
            Stopwatch overallTime = Stopwatch.createStarted();
            log.info("Restcontroller with mapping '/mixpanel/retention' received a request");

            //prepare parameters
            String shortenedWhere = where.replace("properties[", "").replace("]", "").replace("\"", "");
            String wherePropertyShortened = shortenedWhere.substring(0, shortenedWhere.indexOf("=="));
            String wherePropertyValue = shortenedWhere.substring(shortenedWhere.indexOf("==") + "==".length());

            //save request generalized to update the cache based on incoming requests
            ProviderTaskManager manager = new ProviderTaskManager(providerTaskRepository);
            String firstDateInCache;
            Stopwatch dateReferenceDbTime = Stopwatch.createStarted();
            Retention retention = retentionRepository.findFirstByBornEventAndEventAndDomainOrderByDateAsc(born_event, event, wherePropertyValue);

            dateReferenceDbTime.stop();
            retentionEvent.setDateReferenceDbReadTime(dateReferenceDbTime.elapsed(TimeUnit.MILLISECONDS));

            if (retention == null) {
                firstDateInCache = DateStringUtils.today();
            } else {
                firstDateInCache = retention.getDate();
            }
            manager.CreateOrUpdateRetentionTask(ProviderTask.ProviderType.Retention, firstDateInCache, born_event, event, from_date, DateStringUtils.today(), wherePropertyShortened, 5, retentionEvent);


            //get data from cache
            ArrayList<String> dateStringsForRange = DateStringUtils.getDateStringsForRange(from_date, to_date);
            RequestedMetricsProvider provider = new RequestedMetricsProvider();
            Stopwatch recieveRecourceTime = Stopwatch.createStarted();
            MPRetentionApiRedgiant requestedRecource = provider.getRetentionRecource(retentionRepository, born_event, event, wherePropertyValue, dateStringsForRange, retentionEvent);
            recieveRecourceTime.stop();
            retentionEvent.setCompleteRetrieveRequestedDataTime(recieveRecourceTime.elapsed(TimeUnit.MILLISECONDS));

            //performance metrics and logging
            log.info("Restcontroller with mapping '/mixpanel/retention' answeres request with: " + requestedRecource);
            overallTime.stop();
            retentionEvent.setCompleteRequestTime(overallTime.elapsed(TimeUnit.MILLISECONDS));
            LogMetricsUtils.getRetentionEventSeries(metricsSenderService, retentionEvent.getScope(), tags).commitEvent(retentionEvent);

            return requestedRecource;
        }

        @RequestMapping(path = "/segmentation")
        MPSegmentationApi segmentation(@RequestParam String event, @RequestParam String from_date,
                                       @RequestParam String to_date, @RequestParam String where,
                                       @RequestParam(required = false) String on) {
            //performance metrics and logging
            SegmentationEvent segmentationEvent = new SegmentationEvent("_mixpanel_segmentation");
            ArrayList<MetricTag> tags = new ArrayList<>();
            tags.add(new MetricTag("scope", segmentationEvent.getScope()));
            segmentationEvent.setDateRangeInDays(DateStringUtils.getDateStringsForRange(from_date, to_date).size());
            Stopwatch overallTime = Stopwatch.createStarted();
            log.info("Restcontroller with mapping '/mixpanel/segmentation' received a request");

            //prepare parameters
            String shortenedWhere = where.replace("properties[", "").replace("]", "").replace("\"", "");
            String wherePropertyShortened = shortenedWhere.substring(0, shortenedWhere.indexOf("=="));
            String wherePropertyValue = shortenedWhere.substring(shortenedWhere.indexOf("==") + "==".length());
            String shortenedOn = on != null ? on.replace("properties[", "").replace("]", "").replace("\"", "") : "";

            //save request generalized to update the cache based on incoming requests
            ProviderTaskManager manager = new ProviderTaskManager(providerTaskRepository);
            String firstDateInCache;
            Stopwatch dateReferenceDbTime = Stopwatch.createStarted();
            Segmentation segmentation = segmentationRepository.findFirstByEventAndDomainAndBreakdownTypeOrderByDateAsc(event, wherePropertyValue, shortenedOn);

            dateReferenceDbTime.stop();
            segmentationEvent.setDateReferenceDbReadTime(dateReferenceDbTime.elapsed(TimeUnit.MILLISECONDS));

            if (segmentation == null) {
                firstDateInCache = DateStringUtils.today();
            } else {
                firstDateInCache = segmentation.getDate();
            }
            manager.CreateOrUpdateSegmentationTask(ProviderTask.ProviderType.Segmentation, firstDateInCache, event, from_date, DateStringUtils.today(), wherePropertyShortened, shortenedOn, segmentationEvent);

            //get data from cache
            ArrayList<String> dateStringsForRange = DateStringUtils.getDateStringsForRange(from_date, to_date);
            RequestedMetricsProvider provider = new RequestedMetricsProvider();
            Stopwatch recieveRecourceTime = Stopwatch.createStarted();
            MPSegmentationApi requestedRecource = provider.getSegmentationRecource(segmentationRepository, event, wherePropertyValue, shortenedOn, dateStringsForRange, segmentationEvent);
            recieveRecourceTime.stop();
            segmentationEvent.setCompleteRetrieveRequestedDataTime(recieveRecourceTime.elapsed(TimeUnit.MILLISECONDS));

            //performance metrics and logging
            log.info("Restcontroller with mapping '/mixpanel/segmentation' answeres request with: " + requestedRecource);
            overallTime.stop();
            segmentationEvent.setCompleteRequestTime(overallTime.elapsed(TimeUnit.MILLISECONDS));
            LogMetricsUtils.getSegmentationEventSeries(metricsSenderService, segmentationEvent.getScope(), tags).commitEvent(segmentationEvent);

            return requestedRecource;
        }

        @RequestMapping(path = "/funnels")
        MPFunnelsApi funnels(@RequestParam int funnel_id, @RequestParam FunnelRange funnelsDateRange, @RequestParam String where) {
            //performance metrics and logging
            FunnelsEvent funnelsEvent = new FunnelsEvent("_mixpanel_funnels");
            ArrayList<MetricTag> tags = new ArrayList<>();
            tags.add(new MetricTag("scope", funnelsEvent.getScope()));
            tags.add(new MetricTag("FunnelID", String.valueOf(funnel_id)));
            funnelsEvent.setDateRangeInDays(funnelsDateRange.getDateRange());
            Stopwatch overallTime = Stopwatch.createStarted();
            log.info("Restcontroller with mapping '/mixpanel/funnels' received a request");

            //prepare parameters
            String shortenedWhere = where.replace("properties[", "").replace("]", "").replace("\"", "");
            String wherePropertyShortened = shortenedWhere.substring(0, shortenedWhere.indexOf("=="));
            String wherePropertyValue = shortenedWhere.substring(shortenedWhere.indexOf("==") + "==".length());

            //save request generalized to update the cache based on incoming requests
            ProviderTaskManager manager = new ProviderTaskManager(providerTaskRepository);

            manager.CreateOrUpdateFunnelsTask(ProviderTask.ProviderType.Funnels, funnel_id, funnelsDateRange, wherePropertyShortened, funnelsEvent);

            //get data from cache
            RequestedMetricsProvider metricsProvider = new RequestedMetricsProvider();
            Stopwatch recieveRecourceTime = Stopwatch.createStarted();
            MPFunnelsApi requestedRecource = metricsProvider.getFunnelsRecource(funnelsRepository, funnel_id, wherePropertyValue, funnelsDateRange, funnelsEvent);
            recieveRecourceTime.stop();
            funnelsEvent.setCompleteRetrieveRequestedDataTime(recieveRecourceTime.elapsed(TimeUnit.MILLISECONDS));

            //performance metrics and logging
            log.info("Restcontroller with mapping '/mixpanel/funnels' answeres request with: " + requestedRecource);
            overallTime.stop();
            funnelsEvent.setCompleteRequestTime(overallTime.elapsed(TimeUnit.MILLISECONDS));
            LogMetricsUtils.getFunnelsEventSeries(metricsSenderService, funnelsEvent.getScope(), tags).commitEvent(funnelsEvent);
            return requestedRecource;
        }
    }
}