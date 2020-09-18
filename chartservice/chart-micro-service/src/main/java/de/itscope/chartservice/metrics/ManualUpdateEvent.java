package de.itscope.chartservice.metrics;

public class ManualUpdateEvent {

    private final String scope;
    private long completeRequestTime;

    private long DateRangeInDays;

    private long completeUpdateCacheTime;


    public ManualUpdateEvent(String scope) {
        this.scope = scope;
    }

    public long getDateRangeInDays() {
        return DateRangeInDays;
    }

    public void setDateRangeInDays(long dateRangeInDays) {
        DateRangeInDays = dateRangeInDays;
    }

    public long getCompleteUpdateCacheTime() {
        return completeUpdateCacheTime;
    }

    public void setCompleteUpdateCacheTime(long completeUpdateCacheTime) {
        this.completeUpdateCacheTime = completeUpdateCacheTime;
    }

    public String getScope() {
        return scope;
    }

    public long getCompleteRequestTime() {
        return completeRequestTime;
    }

    public void setCompleteRequestTime(long completeRequestTime) {
        this.completeRequestTime = completeRequestTime;
    }

}
