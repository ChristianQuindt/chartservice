package de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"lower",
		"higher",
		"buckets"
})
public class TimeBucketsFromPrev {

	@JsonProperty("lower")
	private Integer lower;
	@JsonProperty("higher")
	private Integer higher;
	@JsonProperty("buckets")
	private List<Integer> buckets = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("lower")
	public Integer getLower() {
		return lower;
	}

	@JsonProperty("lower")
	public void setLower(Integer lower) {
		this.lower = lower;
	}

	@JsonProperty("higher")
	public Integer getHigher() {
		return higher;
	}

	@JsonProperty("higher")
	public void setHigher(Integer higher) {
		this.higher = higher;
	}

	@JsonProperty("buckets")
	public List<Integer> getBuckets() {
		return buckets;
	}

	@JsonProperty("buckets")
	public void setBuckets(List<Integer> buckets) {
		this.buckets = buckets;
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
