package de.itscope.chartservice.model.storage;

import javax.persistence.*;

@Entity
public class Segmentation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long serial;
    private String event;
    private String domain;
    private String breakdownType;
    private String breakdownValue;
    private String date;
    private int value;

    protected Segmentation(){}

    public Segmentation(String event, String domain, String breakdownType, String breakdownValue, String date, int value) {
        this.event = event;
        this.domain = domain;
        this.breakdownType = breakdownType;
        this.breakdownValue = breakdownValue;
        this.date = date;
        this.value = value;
    }

    public long getSerial() {
        return serial;
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

    public String getBreakdownType() {
        return breakdownType;
    }

    public void setBreakdownType(String breakdown) {
        this.breakdownType = breakdown;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Segmentation{" + "serial='" + serial + "'}";
    }

    public String getBreakdownValue() {
        return breakdownValue;
    }

    public void setBreakdownValue(String breakdownValue) {
        this.breakdownValue = breakdownValue;
    }
}
