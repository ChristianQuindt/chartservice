package de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.jqlApi;

import com.fasterxml.jackson.annotation.*;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.MPReturnType;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "key", "value" })
public class MPKeyValueDatum implements MPReturnType {
	@JsonProperty("key")
	private String key;
	@JsonProperty("value")
	private Integer value;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonProperty("key")
	public String getKey() {
		return key;
	}

	@JsonProperty("value")
	public Integer getValue() {
		return value;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@JsonProperty("key")
	public void setKey(String key) {
		this.key = key;
	}

	@JsonProperty("value")
	public void setValue(Integer value) {
		this.value = value;
	}
}
