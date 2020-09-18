package de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi;


import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"operand"
})
public class Filter {

	@JsonProperty("operand")
	private String operand;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("operand")
	public String getOperand() {
		return operand;
	}

	@JsonProperty("operand")
	public void setOperand(String operand) {
		this.operand = operand;
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
