package de.itscope.chartservice.model.storage;

import javax.persistence.*;
import java.util.List;

@Entity
public class Retention {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long serial;
    private String bornEvent;
    private String event;
    private String domain;
    private String date;
    private int first;
    @ElementCollection
    @OrderColumn
    private List<Integer> counts;

    protected Retention() {
    }

    public Retention(String bornEvent, String event, String domain, String date, int first, List<Integer> counts) {
        this.bornEvent = bornEvent;
        this.event = event;
        this.domain = domain;
        this.date = date;
        this.first = first;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public List<Integer> getCounts() {
        return counts;
    }

    public void setCounts(List<Integer> counts) {
        this.counts = counts;
    }

    public long getSerial() {
        return serial;
    }

    @Override
    public String toString() {
        return "Retention{" +
                "serial=" + serial +
                '}';
    }
}
