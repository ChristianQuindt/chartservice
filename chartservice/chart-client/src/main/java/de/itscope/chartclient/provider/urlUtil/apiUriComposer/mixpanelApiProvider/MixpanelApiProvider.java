package de.itscope.chartclient.provider.urlUtil.apiUriComposer.mixpanelApiProvider;


import de.itscope.chartclient.provider.urlUtil.UriComposer;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;

public interface MixpanelApiProvider extends UriComposer {
	String funnelsUri = "/mixpanel/funnels/";
	String segmentationUri = "/mixpanel/segmentation/";
	String retentionUri = "/mixpanel/retention/";

}
