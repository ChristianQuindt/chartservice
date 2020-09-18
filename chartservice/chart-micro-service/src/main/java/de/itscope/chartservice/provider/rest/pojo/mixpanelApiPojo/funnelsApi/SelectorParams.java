package de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi;


import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"step_label",
		"bool_op",
		"property_filter_params_list"
})
public class SelectorParams {

	@JsonProperty("step_label")
	private String stepLabel;
	@JsonProperty("bool_op")
	private String boolOp;
	@JsonProperty("property_filter_params_list")
	private List<PropertyFilterParamsList> propertyFilterParamsList = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("step_label")
	public String getStepLabel() {
		return stepLabel;
	}

	@JsonProperty("step_label")
	public void setStepLabel(String stepLabel) {
		this.stepLabel = stepLabel;
	}

	@JsonProperty("bool_op")
	public String getBoolOp() {
		return boolOp;
	}

	@JsonProperty("bool_op")
	public void setBoolOp(String boolOp) {
		this.boolOp = boolOp;
	}

	@JsonProperty("property_filter_params_list")
	public List<PropertyFilterParamsList> getPropertyFilterParamsList() {
		return propertyFilterParamsList;
	}

	@JsonProperty("property_filter_params_list")
	public void setPropertyFilterParamsList(List<PropertyFilterParamsList> propertyFilterParamsList) {
		this.propertyFilterParamsList = propertyFilterParamsList;
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
