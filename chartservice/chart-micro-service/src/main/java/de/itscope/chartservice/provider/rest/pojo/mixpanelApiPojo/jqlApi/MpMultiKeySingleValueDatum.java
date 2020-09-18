package de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.jqlApi;

import com.fasterxml.jackson.annotation.*;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.MPReturnType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"key",
		"value"
})
public class MpMultiKeySingleValueDatum implements MPReturnType {

	@JsonProperty("key")
	private List<String> key = null;
	@JsonProperty("value")
	private Integer value;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("key")
	public List<String> getKey() {
		return key;
	}

	@JsonProperty("key")
	public void setKey(List<String> key) {
		this.key = key;
	}

	@JsonProperty("value")
	public Integer getValue() {
		return value;
	}

	@JsonProperty("value")
	public void setValue(Integer value) {
		this.value = value;
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