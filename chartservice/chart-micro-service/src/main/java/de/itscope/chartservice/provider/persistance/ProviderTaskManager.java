package de.itscope.chartservice.provider.persistance;

import com.google.common.base.Stopwatch;
import de.itscope.chartservice.metrics.FunnelsEvent;
import de.itscope.chartservice.metrics.RetentionEvent;
import de.itscope.chartservice.metrics.SegmentationEvent;
import de.itscope.chartservice.model.repository.ProviderTaskRepository;
import de.itscope.chartservice.model.storage.ProviderTask;
import de.itscope.chartservice.util.DateStringUtils;
import de.itscope.chartservice.util.funnelRangeUtils.FunnelRange;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class ProviderTaskManager {
    ProviderTaskRepository repository;

    public ProviderTaskManager(ProviderTaskRepository providerTaskRepository) {
        repository = providerTaskRepository;
    }

    public void CreateOrUpdateSegmentationTask(ProviderTask.ProviderType type, String firstRepoEntryDate, String event, String from_date, String to_date, String where, String on, SegmentationEvent segmentationEvent) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            ProviderTask task = repository.findByTypeAndEventAndSpecifierAndBreakdown(type, event, where, on);
            if (task == null) {
                task = new ProviderTask(type, event, from_date, to_date, where, on);
            } else {
                task.increasePriority();
                task.setTo_Date(DateStringUtils.today());
            }
            if (DateStringUtils.isFirstDateBeforeSecond(from_date, task.getFrom_Date())) {
                if (DateStringUtils.isFirstDateBeforeSecond(from_date, firstRepoEntryDate)) {
                    task.setFrom_Date(from_date);
                }
            }
            if (!DateStringUtils.isFirstDateBeforeSecond(task.getFrom_Date(), firstRepoEntryDate)) {
                task.setFrom_Date(DateStringUtils.yesterday());
            }
            repository.save(task);
            stopwatch.stop();
            segmentationEvent.setCompleteUpdateTaskPersistenceTime(stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void CreateOrUpdateRetentionTask(ProviderTask.ProviderType type, String firstRepoEntryDate, String born_event, String event, String from_date, String to_date, String where, int interval_count, RetentionEvent retentionEvent) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ProviderTask task = repository.findByTypeAndBornEventAndEventAndSpecifierAndIntervalCount(type,born_event,event,where,interval_count);
        if (task == null){
            task = new ProviderTask(type, born_event, event, from_date, to_date, interval_count, where);
        } else {
            task.increasePriority();
            task.setTo_Date(DateStringUtils.today());
        }
        try {
            if (DateStringUtils.isFirstDateBeforeSecond(from_date, task.getFrom_Date())){
                if (DateStringUtils.isFirstDateBeforeSecond(from_date, firstRepoEntryDate)){
                    task.setFrom_Date(from_date);
                }
            }
            if (!DateStringUtils.isFirstDateBeforeSecond(task.getFrom_Date(), firstRepoEntryDate)){
                task.setFrom_Date(DateStringUtils.yesterday());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        repository.save(task);
        stopwatch.stop();
        retentionEvent.setCompleteUpdateTaskPersistenceTime(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public void CreateOrUpdateFunnelsTask(ProviderTask.ProviderType type, int funnelID, FunnelRange dateRange, String on, FunnelsEvent requestMetricsEvent) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ProviderTask task = repository.findByTypeAndFunnelIDAndBreakdownAndFunnelRange(type, funnelID, on, dateRange.name());
        if (task == null) {
            task = new ProviderTask(type, funnelID, dateRange, on);
        } else {
            task.increasePriority();
        }
        repository.save(task);
        stopwatch.stop();
        requestMetricsEvent.setCompleteUpdateTaskPersistenceTime(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
