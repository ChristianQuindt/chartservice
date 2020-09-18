package de.itscope.chartservice.metrics;

public class RetentionEvent {
    private final String scope;
    private long completeRequestTime;

    private long dateRangeInDays;
    private long amountOfDataProvided;

    private long dateReferenceDbReadTime;
    private long completeUpdateTaskPersistenceTime;

    private long retrieveRequestedDataDbCallTime;
    private long prepareRequestedDataTime;
    private long completeRetrieveRequestedDataTime;

    public RetentionEvent(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public long getDateRangeInDays() {
        return dateRangeInDays;
    }

    public void setDateRangeInDays(long dateRangeInDays) {
        this.dateRangeInDays = dateRangeInDays;
    }

    public long getAmountOfDataProvided() {
        return amountOfDataProvided;
    }

    public void setAmountOfDataProvided(long amountOfDataProvided) {
        this.amountOfDataProvided = amountOfDataProvided;
    }

    public long getCompleteRequestTime() {
        return completeRequestTime;
    }

    public void setCompleteRequestTime(long completeRequestTime) {
        this.completeRequestTime = completeRequestTime;
    }

    public long getDateReferenceDbReadTime() {
        return dateReferenceDbReadTime;
    }

    public void setDateReferenceDbReadTime(long dateReferenceDbReadTime) {
        this.dateReferenceDbReadTime = dateReferenceDbReadTime;
    }

    public long getCompleteUpdateTaskPersistenceTime() {
        return completeUpdateTaskPersistenceTime;
    }

    public void setCompleteUpdateTaskPersistenceTime(long completeUpdateTaskPersistenceTime) {
        this.completeUpdateTaskPersistenceTime = completeUpdateTaskPersistenceTime;
    }

    public long getRetrieveRequestedDataDbCallTime() {
        return retrieveRequestedDataDbCallTime;
    }

    public void setRetrieveRequestedDataDbCallTime(long retrieveRequestedDataDbCallTime) {
        this.retrieveRequestedDataDbCallTime = retrieveRequestedDataDbCallTime;
    }

    public long getPrepareRequestedDataTime() {
        return prepareRequestedDataTime;
    }

    public void setPrepareRequestedDataTime(long prepareRequestedDataTime) {
        this.prepareRequestedDataTime = prepareRequestedDataTime;
    }

    public long getCompleteRetrieveRequestedDataTime() {
        return completeRetrieveRequestedDataTime;
    }

    public void setCompleteRetrieveRequestedDataTime(long completeRetrieveRequestedDataTime) {
        this.completeRetrieveRequestedDataTime = completeRetrieveRequestedDataTime;
    }
}
