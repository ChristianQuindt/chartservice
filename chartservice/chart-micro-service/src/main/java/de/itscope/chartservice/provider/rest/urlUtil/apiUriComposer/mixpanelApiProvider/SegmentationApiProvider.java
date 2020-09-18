package de.itscope.chartservice.provider.rest.urlUtil.apiUriComposer.mixpanelApiProvider;


import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.segmentationApi.MPSegmentationApi;

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

    public SegmentationApiProvider(String event, String from_date, String to_date, String where, String on) {
        this.event = event;
        this.from_date = from_date;
        this.to_date = to_date;
        if (!where.isEmpty()) {
            this.where = where;
        }
        if (!on.isEmpty()) {
            this.on = on;
        }
    }

    public URI compose() {
        if (where == null) {
            if (on == null) {
                return compose(segmentationUri, "event", event, "from_date", from_date, "to_date", to_date);
            } else
                return compose(segmentationUri, "event", event, "from_date", from_date, "to_date", to_date, "on", on);
        }
        if (on == null) {
            return compose(segmentationUri, "event", event, "from_date", from_date, "to_date", to_date, "where", where);
        }

        return compose(segmentationUri, "event", event, "from_date", from_date, "to_date", to_date, "where",
                where, "on", on);
    }

    public Map<String, Map<String, Integer>> prepareDataStructure(MPSegmentationApi data) {
        return data.getData().getValues().getAdditionalProperties().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> ((Map<String, Integer>) s.getValue())));
    }
}
