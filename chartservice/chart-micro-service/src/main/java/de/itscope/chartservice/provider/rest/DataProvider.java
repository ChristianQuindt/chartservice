package de.itscope.chartservice.provider.rest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;

public class DataProvider {
    /**
     * The Mixpanel Secret is used for authorization and therefore access to a projects data.
     * It must be kept secret. If it got leaked, mixpanel support must be called to rotate the secret.
     */
    private final String mpSecret;

    public DataProvider(String mpSecret) {
        this.mpSecret = mpSecret;
    }

    public <T> T provide(URI uri, Class<T> c) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders authorization = new HttpHeaders() {{
            String auth = mpSecret + ":";
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
        T data = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity(authorization),
                c ).getBody();

        return data;
    }
}
