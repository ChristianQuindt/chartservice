package de.itscope.chartclient.provider.urlUtil.apiUriComposer.mixpanelApiProvider;

import de.itscope.chartclient.model.mixpanelApi.funnelsApi.MPFunnelsApi;
import de.itscope.chartclient.provider.urlUtil.FunnelRange;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class FunnelsApiProvider implements MixpanelApiProvider {
    public static final Class<MPFunnelsApi> returnType = MPFunnelsApi.class;
    private FunnelRange funnelsDateRange;

    private int funnel_id;

    private String where;

    private String baseURL;

    public FunnelsApiProvider(String baseURL, int funnelID, FunnelRange funnelsDateRange, String filterProperty, String filterSpecific) {
        this.baseURL = baseURL;
        this.funnel_id = funnelID;
        this.funnelsDateRange = funnelsDateRange;
        if (!filterProperty.isEmpty() && !filterSpecific.isEmpty()) {
            this.where = "properties[\"" + filterProperty + "\"]==\"" + filterSpecific + "\"";
        }

    }

    @Override
    public URI compose() {
        String encodedUri;
        if (where == null) {
            encodedUri = buildEncodedString(funnelsUri, "funnel_id", String.valueOf(funnel_id), "funnelsDateRange", funnelsDateRange.name());
        } else {
            encodedUri = buildEncodedString(funnelsUri, "funnel_id", String.valueOf(funnel_id), "funnelsDateRange", funnelsDateRange.name(), "where", where);
        }
        return URI.create(baseURL + encodedUri);
    }

    public Map<String, Integer> prepareDataStructure(MPFunnelsApi data) {

        Map<String, Integer> collect = data.getData().getAdditionalProperties().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> (int)s.getValue()));

        return collect;
    }

}
