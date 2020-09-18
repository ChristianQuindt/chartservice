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
		"count",
		"avg_time",
		"event",
		"goal",
		"step_label",
		"selector",
		"selector_params",
		"overall_conv_ratio",
		"step_conv_ratio",
		"time_buckets_from_start",
		"time_buckets_from_prev",
		"custom_event",
		"custom_event_id"
})
public class Step {

	@JsonProperty("count")
	private Integer count;
	@JsonProperty("avg_time")
	private Integer avgTime;
	@JsonProperty("event")
	private String event;
	@JsonProperty("goal")
	private String goal;
	@JsonProperty("step_label")
	private String stepLabel;
	@JsonProperty("selector")
	private String selector;
	@JsonProperty("selector_params")
	private SelectorParams selectorParams;
	@JsonProperty("overall_conv_ratio")
	private Double overallConvRatio;
	@JsonProperty("step_conv_ratio")
	private Double stepConvRatio;
	@JsonProperty("time_buckets_from_start")
	private TimeBucketsFromStart timeBucketsFromStart;
	@JsonProperty("time_buckets_from_prev")
	private TimeBucketsFromPrev timeBucketsFromPrev;
	@JsonProperty("custom_event")
	private Boolean customEvent;
	@JsonProperty("custom_event_id")
	private Integer customEventId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("count")
	public Integer getCount() {
		return count;
	}

	@JsonProperty("count")
	public void setCount(Integer count) {
		this.count = count;
	}

	@JsonProperty("avg_time")
	public Integer getAvgTime() {
		return avgTime;
	}

	@JsonProperty("avg_time")
	public void setAvgTime(Integer avgTime) {
		this.avgTime = avgTime;
	}

	@JsonProperty("event")
	public String getEvent() {
		return event;
	}

	@JsonProperty("event")
	public void setEvent(String event) {
		this.event = event;
	}

	@JsonProperty("goal")
	public String getGoal() {
		return goal;
	}

	@JsonProperty("goal")
	public void setGoal(String goal) {
		this.goal = goal;
	}

	@JsonProperty("step_label")
	public String getStepLabel() {
		return stepLabel;
	}

	@JsonProperty("step_label")
	public void setStepLabel(String stepLabel) {
		this.stepLabel = stepLabel;
	}

	@JsonProperty("selector")
	public String getSelector() {
		return selector;
	}

	@JsonProperty("selector")
	public void setSelector(String selector) {
		this.selector = selector;
	}

	@JsonProperty("selector_params")
	public SelectorParams getSelectorParams() {
		return selectorParams;
	}

	@JsonProperty("selector_params")
	public void setSelectorParams(SelectorParams selectorParams) {
		this.selectorParams = selectorParams;
	}

	@JsonProperty("overall_conv_ratio")
	public Double getOverallConvRatio() {
		return overallConvRatio;
	}

	@JsonProperty("overall_conv_ratio")
	public void setOverallConvRatio(Double overallConvRatio) {
		this.overallConvRatio = overallConvRatio;
	}

	@JsonProperty("step_conv_ratio")
	public Double getStepConvRatio() {
		return stepConvRatio;
	}

	@JsonProperty("step_conv_ratio")
	public void setStepConvRatio(Double stepConvRatio) {
		this.stepConvRatio = stepConvRatio;
	}

	@JsonProperty("time_buckets_from_start")
	public TimeBucketsFromStart getTimeBucketsFromStart() {
		return timeBucketsFromStart;
	}

	@JsonProperty("time_buckets_from_start")
	public void setTimeBucketsFromStart(TimeBucketsFromStart timeBucketsFromStart) {
		this.timeBucketsFromStart = timeBucketsFromStart;
	}

	@JsonProperty("time_buckets_from_prev")
	public TimeBucketsFromPrev getTimeBucketsFromPrev() {
		return timeBucketsFromPrev;
	}

	@JsonProperty("time_buckets_from_prev")
	public void setTimeBucketsFromPrev(TimeBucketsFromPrev timeBucketsFromPrev) {
		this.timeBucketsFromPrev = timeBucketsFromPrev;
	}

	@JsonProperty("custom_event")
	public Boolean getCustomEvent() {
		return customEvent;
	}

	@JsonProperty("custom_event")
	public void setCustomEvent(Boolean customEvent) {
		this.customEvent = customEvent;
	}

	@JsonProperty("custom_event_id")
	public Integer getCustomEventId() {
		return customEventId;
	}

	@JsonProperty("custom_event_id")
	public void setCustomEventId(Integer customEventId) {
		this.customEventId = customEventId;
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
