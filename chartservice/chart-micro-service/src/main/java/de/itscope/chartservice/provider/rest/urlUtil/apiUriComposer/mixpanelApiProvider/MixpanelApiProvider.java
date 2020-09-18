package de.itscope.chartservice.provider.rest.urlUtil.apiUriComposer.mixpanelApiProvider;



import de.itscope.chartservice.provider.rest.urlUtil.UriProvider;

import java.net.URI;

public interface MixpanelApiProvider extends UriProvider {

	String baseURL = "https://mixpanel.com/api/2.0";
	String jqlUri = "/jql/";
	String funnelsUri = "/funnels/";
	String segmentationUri = "/segmentation/";
	String retentionUri = "/retention/";

	default URI compose(String uri, String... args) {
		return URI.create(baseURL + buildEncodedString(uri, args));

	}
}
