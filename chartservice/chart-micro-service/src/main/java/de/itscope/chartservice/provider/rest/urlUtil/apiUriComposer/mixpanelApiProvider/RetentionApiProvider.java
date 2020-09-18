package de.itscope.chartservice.provider.rest.urlUtil.apiUriComposer.mixpanelApiProvider;

import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.funnelsApi.MPFunnelsApi;
import de.itscope.chartservice.provider.rest.pojo.mixpanelApiPojo.retentionApi.MPRetentionApi;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

public class RetentionApiProvider implements MixpanelApiProvider {
    public static final Class<MPRetentionApi> returnType = MPRetentionApi.class;

    private String from_date;
    private String to_date;
    private String retention_type = "birth";
    private String born_event;
    private String event;
    private String unit = "week";
    private int interval_count = 5;
    private String on;

    public RetentionApiProvider(String from_date, String to_date, String born_event, String event, String on) {
        this.from_date = from_date;
        this.to_date = to_date;
        this.born_event = born_event;
        this.event = event;

        if (on != null && !on.isEmpty()) {
            this.on = "properties[\"" + on + "\"]";
        }
    }

    @Override
    public URI compose() {
        if (on == null){
            return compose(retentionUri, "from_date", from_date, "to_date", to_date,"retention_type", retention_type,
                    "born_event", born_event, "event", event, "unit", unit, "interval_count", String.valueOf(interval_count));
        } else {
            return compose(retentionUri, "from_date", from_date, "to_date", to_date,"retention_type", retention_type,
                    "born_event", born_event, "event", event, "unit", unit, "interval_count", String.valueOf(interval_count), "on", on);
        }
    }
}
