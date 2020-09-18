package de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.segmentationApi;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"series",
		"values"
})
public class Data {

	@JsonProperty("series")
	private List<String> series = null;
	@JsonProperty("values")
	private Values values;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("series")
	public List<String> getSeries() {
		return series;
	}

	@JsonProperty("series")
	public void setSeries(List<String> series) {
		this.series = series;
	}

	@JsonProperty("values")
	public Values getValues() {
		return values;
	}

	@JsonProperty("values")
	public void setValues(Values values) {
		this.values = values;
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