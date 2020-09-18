package de.itscope.chartclient.model.mixpanelApi.funnelsApi;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"first",
		"last"
})
public class IncompleteBuckets {

	@JsonProperty("first")
	private Boolean first;
	@JsonProperty("last")
	private Boolean last;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("first")
	public Boolean getFirst() {
		return first;
	}

	@JsonProperty("first")
	public void setFirst(Boolean first) {
		this.first = first;
	}

	@JsonProperty("last")
	public Boolean getLast() {
		return last;
	}

	@JsonProperty("last")
	public void setLast(Boolean last) {
		this.last = last;
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
