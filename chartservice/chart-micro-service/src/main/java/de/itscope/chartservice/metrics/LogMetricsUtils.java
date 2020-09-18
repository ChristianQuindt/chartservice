package de.itscope.chartservice.metrics;

import net.monofraps.influxmetrics.EventSeries;
import net.monofraps.influxmetrics.MetricTag;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class LogMetricsUtils {

    public static EventSeries<ManualUpdateEvent> getManualUpdateEventSeries(MetricsSenderService metricsSenderService,String measurementName, List<MetricTag> tags) {
        return metricsSenderService.getRequestServiceMetricsRegistry().eventSeries(measurementName, tags, ManualUpdateEvent.class);
    }

    public static EventSeries<FunnelsEvent> getFunnelsEventSeries(MetricsSenderService metricsSenderService,String measurementName, List<MetricTag> tags) {
        return metricsSenderService.getRequestServiceMetricsRegistry().eventSeries(measurementName, tags, FunnelsEvent.class);
    }

    public static EventSeries<SegmentationEvent> getSegmentationEventSeries(MetricsSenderService metricsSenderService,String measurementName, List<MetricTag> tags) {
        return metricsSenderService.getRequestServiceMetricsRegistry().eventSeries(measurementName, tags, SegmentationEvent.class);
    }

    public static EventSeries<RetentionEvent> getRetentionEventSeries(MetricsSenderService metricsSenderService,String measurementName, List<MetricTag> tags) {
        return metricsSenderService.getRequestServiceMetricsRegistry().eventSeries(measurementName, tags, RetentionEvent.class);
    }

    public static EventSeries<SingleTaskEvent> getSingleTaskEventSeries(MetricsSenderService metricsSenderService,String measurementName, List<MetricTag> tags) {
        return metricsSenderService.getRequestServiceMetricsRegistry().eventSeries(measurementName, tags, SingleTaskEvent.class);
    }

    public static EventSeries<UpdateCacheEvent> getUpdateCacheEventSeries(MetricsSenderService metricsSenderService,String measurementName, List<MetricTag> tags) {
        return metricsSenderService.getRequestServiceMetricsRegistry().eventSeries(measurementName, tags, UpdateCacheEvent.class);
    }

}
