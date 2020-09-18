package de.itscope.chartclient.model.mixpanelApi.segmentationApi;

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
		"data",
		"legend_size",
		"computed_at"
})
public class MPSegmentationApi implements MPReturnType {

	@JsonProperty("data")
	private Data data;
	@JsonProperty("legend_size")
	private Integer legendSize;
	@JsonProperty("computed_at")
	private String computedAt;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("data")
	public Data getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(Data data) {
		this.data = data;
	}

	@JsonProperty("legend_size")
	public Integer getLegendSize() {
		return legendSize;
	}

	@JsonProperty("legend_size")
	public void setLegendSize(Integer legendSize) {
		this.legendSize = legendSize;
	}

	@JsonProperty("computed_at")
	public String getComputedAt() {
		return computedAt;
	}

	@JsonProperty("computed_at")
	public void setComputedAt(String computedAt) {
		this.computedAt = computedAt;
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