package de.itscope.chartclient.provider.urlUtil.apiUriComposer;

import de.itscope.chartclient.provider.urlUtil.EncodingUtil;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class UpdateCacheApiProvider{
    private String baseURL;
    private String event;
    private ApiUris uri;
    private int funnel_id;
    private String from_date;
    private String to_date;
    private String filterProperty;
    private String filterSpecific;
    private String breakdownProperty;
    private String born_event;

    public void updateMixpanelSegmentationApiCache(String baseURL ,String event, String from_date, String to_date, String filterProperty, String filterSpecific, String breakdownProperty) {
        this.baseURL = baseURL + "/update";
        this.event = event;
        this.from_date = from_date;
        this.to_date = to_date;
        this.filterProperty = filterProperty;
        this.filterSpecific = filterSpecific;
        this.breakdownProperty = breakdownProperty;
        this.uri = ApiUris.MixpanelSegmentation;
        URI composedURI = composeSegmentationURI();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(composedURI, HttpMethod.POST, null,
                String.class);
    }

    public UpdateCacheApiProvider(String baseURL, String event, String from_date, String to_date, String filterProperty, String filterSpecific, String born_event) {
        this.baseURL = baseURL + "/update";
        this.event = event;
        this.from_date = from_date;
        this.to_date = to_date;
        this.filterProperty = filterProperty;
        this.filterSpecific = filterSpecific;
        this.born_event = born_event;
        this.uri = ApiUris.MixpanelRetention;
        URI composedURI = composeRetentionURI();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(composedURI, HttpMethod.POST, null,
                String.class);
    }

    public void updateMixpanelFunnelsApiCache(String baseURL, int funnel_id, String from_date, String to_date, String filterProperty, String filterSpecific) {
        this.baseURL = baseURL + "/update";
        this.funnel_id = funnel_id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.filterProperty = filterProperty;
        this.filterSpecific = filterSpecific;
        this.uri = ApiUris.MixpanelFunnels;
        URI composedURI = composeFunnelUri();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(composedURI, HttpMethod.POST, null,
                String.class);
    }

    private URI composeRetentionURI() {
        String encodedUri;
        String where = "properties[\"" + filterProperty + "\"]==\"" + filterSpecific + "\"";
        encodedUri = buildEncodedString(uri.getUri(), "from_date", from_date, "to_date", to_date,
                "born_event", born_event, "event", event, "where", where);
        return URI.create(baseURL + encodedUri);
    }

    public URI composeSegmentationURI(){
        String encodedUri;
        String where = null, on = null;
        if (!filterProperty.isEmpty() && !filterSpecific.isEmpty()) {
            where = "properties[\"" + filterProperty + "\"]==\"" + filterSpecific + "\"";
        }
        if (!breakdownProperty.isEmpty()) {
            on = "properties[\"" + breakdownProperty + "\"]";
        }
        if (where == null) {
            if (on == null) {
                encodedUri = buildEncodedString(uri.getUri(), "event", event, "from_date", from_date, "to_date", to_date);
            } else
                encodedUri = buildEncodedString(uri.getUri(), "event", event, "from_date", from_date, "to_date", to_date, "on", on);
        } else if (on == null) {
            encodedUri = buildEncodedString(uri.getUri(), "event", event, "from_date", from_date, "to_date", to_date, "where", where);
        } else {
            encodedUri = buildEncodedString(uri.getUri(), "event", event, "from_date", from_date, "to_date", to_date, "where",
                    where, "on", on);
        }

        return URI.create(baseURL + encodedUri);
    }

    private URI composeFunnelUri() {
        String encodedUri;
        String where = null;
        if (!filterProperty.isEmpty() && !filterSpecific.isEmpty()) {
            where = "properties[\"" + filterProperty + "\"]==\"" + filterSpecific + "\"";
        }

        if (where == null) {
            encodedUri = buildEncodedString(uri.getUri(), "funnel_id", String.valueOf(funnel_id), "from_date", from_date, "to_date", to_date);
        } else {
            encodedUri = buildEncodedString(uri.getUri(), "funnel_id", String.valueOf(funnel_id), "from_date", from_date, "to_date", to_date, "where", where);
        }

        return URI.create(baseURL + encodedUri);
    }

    private String buildEncodedString(String uri, String... args) {
        if (args.length > 1) {
            String uriWithParams = uri + "?" + args[0] + "=" + EncodingUtil.encodeURIComponent(args[1]);
            for (int i = 2; i < args.length; i = i + 2) {
                if (args.length > i + 1 && !args[i + 1].isEmpty()) {
                    uriWithParams += "&" + args[i] + "=" + EncodingUtil.encodeURIComponent(args[i + 1]);
                }
            }
            return uriWithParams;
        } else {
            return uri;
        }
    }


    public enum ApiUris {
        MixpanelSegmentation("/mixpanel/segmentation"),
        MixpanelFunnels("/mixpanel/funnels"),
        MixpanelRetention("/mixpanel/retention");
        private final String uri;

        private ApiUris(String uri) {
            this.uri = uri;
        }

        public String getUri() {
            return uri;
        }
    }
}
