package de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
