package com.greennext.solarestimater.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
//@ConfigurationProperties(prefix = "ksolar")
public class PropertiesUtil {

    @Value("${inverter.url}")
    private String inverterUrl;
    @Value("${company.key}")
    private String companyKey;

}
