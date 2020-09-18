package de.itscope.chartclient.provider;

import de.itscope.chartclient.model.RestReturnType;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DataProvider {
    /**
     * The Mixpanel Secret is used for authorization and therefore access to a projects data.
     * It must be kept secret. If it got leaked, mixpanel support must be called to rotate the secret.
     */
    private final String basicAuth;

    public DataProvider(String basicAuth) {
        this.basicAuth = basicAuth;
    }

    public <T extends RestReturnType> T provide(URI uri, Class<T> c) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders authorization = new HttpHeaders() {{
            String auth = basicAuth;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
        T data = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity(authorization),
                c).getBody();

        return data;
    }
}
