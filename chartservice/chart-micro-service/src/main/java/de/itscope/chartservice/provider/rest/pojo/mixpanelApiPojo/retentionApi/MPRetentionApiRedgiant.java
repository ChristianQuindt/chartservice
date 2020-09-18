package de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.retentionApi;

public class MPRetentionApiRedgiant {
    private int[][] counts;
    private String bornEvent;
    private String event;
    private String[] dates;
    private int[] first;

    public MPRetentionApiRedgiant(int[][] counts, String bornEvent, String event, String[] dates, int[] first) {
        this.counts = counts;
        this.bornEvent = bornEvent;
        this.event = event;
        this.dates = dates;
        this.first = first;
    }

    public int[][] getCounts() {
        return counts;
    }

    public void setCounts(int[][] counts) {
        this.counts = counts;
    }

    public String getBornEvent() {
        return bornEvent;
    }

    public void setBornEvent(String bornEvent) {
        this.bornEvent = bornEvent;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String[] getDates() {
        return dates;
    }

    public void setDates(String[] dates) {
        this.dates = dates;
    }

    public int[] getFirst() {
        return first;
    }

    public void setFirst(int[] first) {
        this.first = first;
    }
}
