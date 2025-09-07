package com.greennext.solarestimater.webclient;

import com.greennext.solarestimater.model.response.SampleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

public class GreenNxtWebClientTest {
    @Test
    public void testGetMethod() {
        // Create WebClient.Builder (no base URL for demonstration)
        WebClient.Builder builder = WebClient.builder();
        GreenNxtWebClient client = new GreenNxtWebClient(builder);

        // Sample URL and query params (should be a valid endpoint in a real test)
        String url = "/api/sample";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("param1", "value1");
        queryParams.put("param2", "value2");

        // Prepare response body type
        SampleResponse responseBody = new SampleResponse();

        // Call get method
        try {
            SampleResponse response = (SampleResponse) client.get(url, responseBody, queryParams);
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

