package de.itscope.chartclient.model.mixpanelApi.jqlApi;

import java.util.HashMap;
import java.util.List;
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