package de.itscope.chartservice.provider.rest.urlUtil;

import de.itscope.chartservice.util.EncodingUtil;

import java.net.URI;

public interface UriProvider {

    URI compose();

    URI compose(String uri, String... args);

    default String buildEncodedString(String uri, String... args) {
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
}
