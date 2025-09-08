package com.greennext.solarestimater.apis;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ShineMonitorApis {
    String baseUrl = "http://api.shinemonitor.com/public/";
    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(baseUrl).build();
    }

    public Object fetchCustomerDetails() {
        return null;
    }

}
