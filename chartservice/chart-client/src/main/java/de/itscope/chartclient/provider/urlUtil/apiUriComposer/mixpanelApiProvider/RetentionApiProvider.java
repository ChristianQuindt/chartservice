package de.itscope.chartclient.provider.urlUtil.apiUriComposer.mixpanelApiProvider;

import de.itscope.chartclient.model.mixpanelApi.retentionApi.MPRetentionApi;

import java.net.URI;
import java.util.Map;

public class RetentionApiProvider implements MixpanelApiProvider {
    public static final Class<MPRetentionApi> returnType = MPRetentionApi.class;

    private String baseURL;
    private String from_date;
    private String to_date;
    private String born_event;
    private String event;
    private String where;

    public RetentionApiProvider(String baseURL, String from_date, String to_date, String born_event, String event, String filterProperty, String filterSpecific) {
        this.baseURL = baseURL;
        this.from_date = from_date;
        this.to_date = to_date;
        this.born_event = born_event;
        this.event = event;
        this.where = "properties[\"" + filterProperty + "\"]==\"" + filterSpecific + "\"";

    }

    @Override
    public URI compose() {
        String encodedUri = buildEncodedString(retentionUri, "from_date", from_date, "to_date", to_date,
                "born_event", born_event, "event", event, "where", where);
        return URI.create(baseURL + encodedUri);
    }
}
