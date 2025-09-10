package com.greennext.solarestimater.controller;

import com.greennext.solarestimater.service.PowerGeneratedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GreenNxtController {

    @Autowired
    PowerGeneratedService powerGeneratedService;

    @GetMapping("/hello")
    public String fetchCustomerData() {
        return "Hello World";
    }

    @GetMapping
    public void getGenerationByDate() {

    }

    @GetMapping("/queryAllPlant/{userName}")
    public ResponseEntity<?> queryAllPlants(@PathVariable String userName) {
        log.info("Received request to query all plants for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.queryAllPlants(userName));
    }

    @GetMapping("/energyByDay/{userName}")
    public ResponseEntity<?> queryDailyGeneration(@PathVariable String userName) {
        log.info("Received request to query daily power generation for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyByDay(userName, null));
    }
}
