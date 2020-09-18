package de.itscope.chartclient.model.mixpanelApi.retentionApi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.itscope.chartclient.model.mixpanelApi.MPReturnType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "counts",
        "bornEvent",
        "event",
        "dates",
        "first"
})
public class MPRetentionApi implements MPReturnType {
    @JsonProperty("counts")
    private int[][] counts;
    @JsonProperty("bornEvent")
    private String bornEvent;
    @JsonProperty("event")
    private String event;
    @JsonProperty("dates")
    private String[] dates;
    @JsonProperty("first")
    private int[] first;

    @JsonProperty("counts")
    public int[][] getCounts() {
        return counts;
    }

    @JsonProperty("counts")
    public void setCounts(int[][] counts) {
        this.counts = counts;
    }

    @JsonProperty("bornEvent")
    public String getBornEvent() {
        return bornEvent;
    }

    @JsonProperty("bornEvent")
    public void setBornEvent(String bornEvent) {
        this.bornEvent = bornEvent;
    }

    @JsonProperty("event")
    public String getEvent() {
        return event;
    }

    @JsonProperty("event")
    public void setEvent(String event) {
        this.event = event;
    }

    @JsonProperty("dates")
    public String[] getDates() {
        return dates;
    }

    @JsonProperty("dates")
    public void setDates(String[] dates) {
        this.dates = dates;
    }

    @JsonProperty("first")
    public int[] getFirst() {
        return first;
    }

    @JsonProperty("first")
    public void setFirst(int[] first) {
        this.first = first;
    }
}
