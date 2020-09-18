package de.itscope.chartservice.metrics;

public class UpdateCacheEvent {
    private final String scope;
    private long findUpdateCacheTasksTime;
    private long tasksInBatch;
    private long completeUpdateCacheTime;

    public UpdateCacheEvent(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public long getFindUpdateCacheTasksTime() {
        return findUpdateCacheTasksTime;
    }

    public void setFindUpdateCacheTasksTime(long findUpdateCacheTasksTime) {
        this.findUpdateCacheTasksTime = findUpdateCacheTasksTime;
    }

    public long getTasksInBatch() {
        return tasksInBatch;
    }

    public void setTasksInBatch(long tasksInBatch) {
        this.tasksInBatch = tasksInBatch;
    }

    public long getCompleteUpdateCacheTime() {
        return completeUpdateCacheTime;
    }

    public void setCompleteUpdateCacheTime(long completeUpdateCacheTime) {
        this.completeUpdateCacheTime = completeUpdateCacheTime;
    }
}
