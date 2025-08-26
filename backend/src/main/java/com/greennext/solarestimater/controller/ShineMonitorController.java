package com.greennext.solarestimater.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ShineMonitorController {

    @GetMapping("/hello")
    public String fetchCustomerData() {

        return "Hello World";
    }

}
