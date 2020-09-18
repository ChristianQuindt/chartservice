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
		"completion",
		"starting_amount",
		"steps",
		"worst"
})
public class Analysis {

	@JsonProperty("completion")
	private Integer completion;
	@JsonProperty("starting_amount")
	private Integer startingAmount;
	@JsonProperty("steps")
	private Integer steps;
	@JsonProperty("worst")
	private Integer worst;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("completion")
	public Integer getCompletion() {
		return completion;
	}

	@JsonProperty("completion")
	public void setCompletion(Integer completion) {
		this.completion = completion;
	}

	@JsonProperty("starting_amount")
	public Integer getStartingAmount() {
		return startingAmount;
	}

	@JsonProperty("starting_amount")
	public void setStartingAmount(Integer startingAmount) {
		this.startingAmount = startingAmount;
	}

	@JsonProperty("steps")
	public Integer getSteps() {
		return steps;
	}

	@JsonProperty("steps")
	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	@JsonProperty("worst")
	public Integer getWorst() {
		return worst;
	}

	@JsonProperty("worst")
	public void setWorst(Integer worst) {
		this.worst = worst;
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
