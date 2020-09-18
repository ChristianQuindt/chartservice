package de.itscope.chartclient.provider.urlUtil.apiUriComposer.mixpanelApiProvider;

import de.itscope.chartclient.model.mixpanelApi.segmentationApi.MPSegmentationApi;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

public class SegmentationApiProvider implements MixpanelApiProvider {

    public static final Class<MPSegmentationApi> returnType = MPSegmentationApi.class;

    private String event;

    private String from_date;

    private String to_date;

    private String where;

    private String on;

    private String baseURL;

    public SegmentationApiProvider(String baseURL, String event, String from_date, String to_date, String filterProperty, String filterSpecific, String breakdownProperty) {
        this.baseURL = baseURL;
        this.event = event;
        this.from_date = from_date;
        this.to_date = to_date;
        if (!filterProperty.isEmpty() && !filterSpecific.isEmpty()) {
            this.where = "properties[\"" + filterProperty + "\"]==\"" + filterSpecific + "\"";
        }
        if (!breakdownProperty.isEmpty()) {
            this.on = "properties[\"" + breakdownProperty + "\"]";
        }
    }

    public SegmentationApiProvider(String baseURL, String event, String from_date, String to_date, String filterProperty, String filterSpecific) {
        this.baseURL = baseURL;
        this.event = event;
        this.from_date = from_date;
        this.to_date = to_date;
        if (!filterProperty.isEmpty() && !filterSpecific.isEmpty()) {
            this.where = "properties[\"" + filterProperty + "\"]==\"" + filterSpecific + "\"";
        }
    }

    @Override
    public URI compose() {
        String encodedUri;
        if (where == null) {
            if (on == null) {
                encodedUri = buildEncodedString(segmentationUri, "event", event, "from_date", from_date, "to_date", to_date);
            } else
                encodedUri = buildEncodedString(segmentationUri, "event", event, "from_date", from_date, "to_date", to_date, "on", on);
        } else if (on == null) {
            encodedUri = buildEncodedString(segmentationUri, "event", event, "from_date", from_date, "to_date", to_date, "where", where);
        } else {
            encodedUri = buildEncodedString(segmentationUri, "event", event, "from_date", from_date, "to_date", to_date, "where",
                    where, "on", on);
        }

        return URI.create(baseURL + encodedUri);
    }

    public Map<String, Map<String, Integer>> prepareDataStructure(MPSegmentationApi data) {
        return data.getData().getValues().getAdditionalProperties().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> ((Map<String, Integer>) s.getValue())));
    }
}
