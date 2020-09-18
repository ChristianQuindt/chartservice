package de.itscope.chartclient.model.mixpanelApi.funnelsApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"dates",
		"min_sampling_factor",
		"trend_unit",
		"incomplete_buckets"
})
public class Meta {

	@JsonProperty("dates")
	private List<String> dates = null;
	@JsonProperty("min_sampling_factor")
	private Integer minSamplingFactor;
	@JsonProperty("trend_unit")
	private String trendUnit;
	@JsonProperty("incomplete_buckets")
	private IncompleteBuckets incompleteBuckets;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("dates")
	public List<String> getDates() {
		return dates;
	}

	@JsonProperty("dates")
	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	@JsonProperty("min_sampling_factor")
	public Integer getMinSamplingFactor() {
		return minSamplingFactor;
	}

	@JsonProperty("min_sampling_factor")
	public void setMinSamplingFactor(Integer minSamplingFactor) {
		this.minSamplingFactor = minSamplingFactor;
	}

	@JsonProperty("trend_unit")
	public String getTrendUnit() {
		return trendUnit;
	}

	@JsonProperty("trend_unit")
	public void setTrendUnit(String trendUnit) {
		this.trendUnit = trendUnit;
	}

	@JsonProperty("incomplete_buckets")
	public IncompleteBuckets getIncompleteBuckets() {
		return incompleteBuckets;
	}

	@JsonProperty("incomplete_buckets")
	public void setIncompleteBuckets(IncompleteBuckets incompleteBuckets) {
		this.incompleteBuckets = incompleteBuckets;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
