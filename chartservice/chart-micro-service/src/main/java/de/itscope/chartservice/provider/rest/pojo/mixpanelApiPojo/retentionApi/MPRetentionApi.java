package de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.retentionApi;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.MPReturnType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MPRetentionApi implements MPReturnType {
    @JsonIgnore
    Map<String, DomainValue> dateToDomain = new HashMap<>();

    @JsonAnyGetter
    public Map<String, DomainValue> getDateToDomain() {
        return dateToDomain;
    }

    @JsonAnySetter
    public void setDateToDomain(String name, DomainValue value) {
        this.dateToDomain.put(name, value);
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DomainValue {

        @JsonIgnore
        private Map<String, Values> domainsToValue = new HashMap<>();

        @JsonAnyGetter
        public Map<String, Values> getDomainsToValue() {
            return domainsToValue;
        }

        @JsonAnySetter
        public void setDomainsToValue(String name, Values value) {
            this.domainsToValue.put(name, value);
        }


    }

    public static class Values {
        int first;

        List<Integer> counts;

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

    }
}
