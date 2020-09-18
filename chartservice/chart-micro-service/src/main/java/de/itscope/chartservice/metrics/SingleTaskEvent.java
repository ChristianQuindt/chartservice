package de.itscope.chartservice.metrics;

public class SingleTaskEvent {
    private final String scope;
    private String singleTaskSerial;

    private int dateRangeInDays;
    private long amountOfDataRequested;
    private long amountOfRequestedDataUsed;

    private long requestCacheableDataTime;
    private long updateDbTableEntryTime;

    public SingleTaskEvent(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public String getSingleTaskSerial() {
        return singleTaskSerial;
    }

    public void setSingleTaskSerial(String singleTaskSerial) {
        this.singleTaskSerial = singleTaskSerial;
    }

    public long getRequestCacheableDataTime() {
        return requestCacheableDataTime;
    }

    public void setRequestCacheableDataTime(long requestCacheableDataTime) {
        this.requestCacheableDataTime = requestCacheableDataTime;
    }

    public int getDateRangeInDays() {
        return dateRangeInDays;
    }

    public void setDateRangeInDays(int dateRangeInDays) {
        this.dateRangeInDays = dateRangeInDays;
    }

    public long getAmountOfDataRequested() {
        return amountOfDataRequested;
    }

    public void setAmountOfDataRequested(long amountOfDataRequested) {
        this.amountOfDataRequested = amountOfDataRequested;
    }

    public long getAmountOfRequestedDataUsed() {
        return amountOfRequestedDataUsed;
    }

    public void setAmountOfRequestedDataUsed(long amountOfRequestedDataUsed) {
        this.amountOfRequestedDataUsed = amountOfRequestedDataUsed;
    }

    public long getUpdateDbTableEntryTime() {
        return updateDbTableEntryTime;
    }

    public void setUpdateDbTableEntryTime(long updateDbTableEntryTime) {
        this.updateDbTableEntryTime = updateDbTableEntryTime;
    }

}
