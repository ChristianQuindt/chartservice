package de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"filter",
		"property",
		"selected_property_type",
		"type"
})
public class PropertyFilterParamsList {

	@JsonProperty("filter")
	private Filter filter;
	@JsonProperty("property")
	private Property property;
	@JsonProperty("selected_property_type")
	private String selectedPropertyType;
	@JsonProperty("type")
	private String type;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("filter")
	public Filter getFilter() {
		return filter;
	}

	@JsonProperty("filter")
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	@JsonProperty("property")
	public Property getProperty() {
		return property;
	}

	@JsonProperty("property")
	public void setProperty(Property property) {
		this.property = property;
	}

	@JsonProperty("selected_property_type")
	public String getSelectedPropertyType() {
		return selectedPropertyType;
	}

	@JsonProperty("selected_property_type")
	public void setSelectedPropertyType(String selectedPropertyType) {
		this.selectedPropertyType = selectedPropertyType;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
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
