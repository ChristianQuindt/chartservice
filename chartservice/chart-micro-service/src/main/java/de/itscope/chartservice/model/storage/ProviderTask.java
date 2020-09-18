package de.itscope.chartservice.model.storage;

import de.itscope.chartservice.util.funnelRangeUtils.FunnelRange;
import org.joda.time.DateTime;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.stream.Stream;

@Entity
public class ProviderTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long serial;
    private int priority;
    private long lastChange;
    private String from_Date;
    private String to_Date;
    private int funnelID;
    private String funnelRange;
    private String event;
    @Nullable
    private String specifier;
    private String breakdown;
    private ProviderType type;

    private String bornEvent;
    private int intervalCount;

    //funnel
    public ProviderTask(ProviderType type, int funnelID, FunnelRange funnelRange, String breakdown) {
        this.funnelRange = funnelRange.name();
        this.funnelID = funnelID;
        this.breakdown = breakdown;
        this.type = type;
        resetPriority();
        increasePriority();
    }

    //segmentation
    public ProviderTask(ProviderType type, String event, String from_Date, String to_Date, String specifier, String breakdown) {
        this.type = type;
        this.event = event;
        this.from_Date = from_Date;
        this.to_Date = to_Date;
        this.specifier = specifier;
        this.breakdown = breakdown;
        resetPriority();
        increasePriority();
    }

    //retention
    public ProviderTask(ProviderType type, String born_event, String event, String from_Date, String to_Date, int intervalCount, String specifier) {
        this.from_Date = from_Date;
        this.to_Date = to_Date;
        this.event = event;
        if (intervalCount< 5){
            this.intervalCount = 5;
        } else {
            this.intervalCount = intervalCount;
        }
        this.specifier = specifier;
        this.type = type;
        this.bornEvent = born_event;
        resetPriority();
        increasePriority();
    }

    protected ProviderTask() {
    }

    public String getBornEvent() {
        return bornEvent;
    }

    public void setBornEvent(String born_event) {
        this.bornEvent = born_event;
    }

    public int getIntervalCount() {
        return intervalCount;
    }

    public void setIntervalCount(int interval_count) {
        this.intervalCount = interval_count;
    }

    public FunnelRange getFunnelRange() {
        if (funnelRange == null) {
            return null;
        }
        return Stream.of(FunnelRange.values())
                .filter(c -> c.name().equals(funnelRange))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void setFunnelRange(FunnelRange range) {
        this.funnelRange = range.name();
    }

    public void resetPriority() {
        this.priority = 0;
        updateLastChange();
    }

    public void increasePriority() {
        this.priority++;
        updateLastChange();
    }

    public void updateLastChange() {
        this.lastChange = DateTime.now().getMillis();
    }

    public long getSerial() {
        return serial;
    }

    public int getPriority() {
        return priority;
    }

    public long getLastChange() {
        return lastChange;
    }

    public String getFrom_Date() {
        return from_Date;
    }

    public void setFrom_Date(String from_Date) {
        this.from_Date = from_Date;
        updateLastChange();
    }

    public String getTo_Date() {
        return to_Date;
    }

    public void setTo_Date(String to_Date) {
        this.to_Date = to_Date;
        updateLastChange();
    }

    public int getFunnelID() {
        return funnelID;
    }

    public void setFunnelID(int funnelID) {
        this.funnelID = funnelID;
        updateLastChange();
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
        updateLastChange();
    }

    public String getSpecifier() {
        return specifier;
    }

    public void setSpecifier(String specifier) {
        this.specifier = specifier;
        updateLastChange();
    }

    public String getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(String breakdown) {
        this.breakdown = breakdown;
        updateLastChange();
    }

    public ProviderType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ProviderTask{" +
                "serial=" + serial +
                '}';
    }

    public enum ProviderType {
        Segmentation,
        Funnels,
        Retention;
    }

}
