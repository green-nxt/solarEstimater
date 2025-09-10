package com.greennext.solarestimater.webclient;

import com.greennext.solarestimater.model.response.WebClientResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
@Slf4j
public class GreenNxtWebClient {
    private final WebClient webClient;

    public GreenNxtWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    private void validateParameters(String url, WebClientResponseBody responseBody) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL must not be null or empty");
        }
        if (responseBody == null) {
            throw new IllegalArgumentException("Response body must not be null");
        }
    }

    private URI buildUri(String url, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(url);
        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }
        URI uri = builder.build().toUri();
        log.info("Built URI: {}", uri);
        return uri;
    }

    private WebClientResponseBody getResponse(String url, WebClientResponseBody responseBody, Map<String, String> queryParams) {
        validateParameters(url, responseBody);
        try {
            return webClient.get()
                    .uri(buildUri(url, queryParams))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), clientResponse ->
                        clientResponse.bodyToMono(String.class).map(body ->
                            new RuntimeException("Request failed with status: " + clientResponse.statusCode() + ", body: " + body)
                        )
                    )
                    .bodyToMono(responseBody.getClass())
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("WebClient GET request failed: " + e.getMessage(), e);
        }
    }

    public WebClientResponseBody get(String url, WebClientResponseBody responseBody, Map<String, String> queryParams) {
        return getResponse(url, responseBody, queryParams);
    }
}
