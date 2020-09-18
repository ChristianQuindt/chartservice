package de.itscope.chartclient.model.mixpanelApi.funnelsApi;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.itscope.chartclient.model.mixpanelApi.MPReturnType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"computed_at",
		"data",
		"meta",
		"min_sampling_factor"
})
public class MPFunnelsApi implements MPReturnType {

	@JsonProperty("computed_at")
	private String computedAt;
	@JsonProperty("data")
	private Data data;
	@JsonProperty("meta")
	private Meta meta;
	@JsonProperty("min_sampling_factor")
	private Integer minSamplingFactor;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("computed_at")
	public String getComputedAt() {
		return computedAt;
	}

	@JsonProperty("computed_at")
	public void setComputedAt(String computedAt) {
		this.computedAt = computedAt;
	}

	@JsonProperty("data")
	public Data getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(Data data) {
		this.data = data;
	}

	@JsonProperty("meta")
	public Meta getMeta() {
		return meta;
	}

	@JsonProperty("meta")
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	@JsonProperty("min_sampling_factor")
	public Integer getMinSamplingFactor() {
		return minSamplingFactor;
	}

	@JsonProperty("min_sampling_factor")
	public void setMinSamplingFactor(Integer minSamplingFactor) {
		this.minSamplingFactor = minSamplingFactor;
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
