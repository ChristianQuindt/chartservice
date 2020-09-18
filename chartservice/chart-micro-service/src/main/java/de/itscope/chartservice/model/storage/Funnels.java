package de.itscope.chartservice.model.storage;

import de.itscope.chartservice.util.funnelRangeUtils.FunnelRange;

import javax.persistence.*;
import java.util.stream.Stream;

@Entity
public class Funnels {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int serial;
    private int funnelID;
    private String domain;
    private String stepLabel;
    private int count;
    private String funnelRange;

    public Funnels(int funnelID, String domain, String stepLabel, FunnelRange funnelRange, int count) {
        this.funnelID = funnelID;
        this.domain = domain;
        this.funnelRange = funnelRange.name();
        this.stepLabel = stepLabel;
        this.count = count;
    }

    protected Funnels() {
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

    public long getSerial() {
        return serial;
    }

    public int getFunnelID() {
        return funnelID;
    }

    public void setFunnelID(int funnelID) {
        this.funnelID = funnelID;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    public String getStepLabel() {
        return stepLabel;
    }

    public void setStepLabel(String stepLabel) {
        this.stepLabel = stepLabel;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Funnels{serial ='" + serial + "'";
    }
}
