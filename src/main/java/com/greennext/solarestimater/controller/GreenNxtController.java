package com.greennext.solarestimater.controller;

import com.greennext.solarestimater.service.PowerGeneratedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
public class GreenNxtController {

    @Autowired
    PowerGeneratedService powerGeneratedService;

    @GetMapping("/plant/all")
    public ResponseEntity<?> getAllPlantsDetails(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to query all plants for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.queryAllPlants(userName));
    }

    @GetMapping("/generation/daily")
    public ResponseEntity<?> getDailyGeneration(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to query daily power generation for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyDaily(userName));
    }

    @GetMapping("/generation/monthly")
    public ResponseEntity<?> getMonthlyGeneration(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to query monthly power generation for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyMonthly(userName));
    }

    @GetMapping("/generation/month/{date}")
    public ResponseEntity<?> getMonthlyGeneration(Authentication authentication, @PathVariable LocalDate date) {
        String userName = authentication.getName();
        log.info("Received request to query power generation for month {} for user: {}", date, userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyByMonth(userName, date));
    }

    @GetMapping("/generation/date/{date}")
    public ResponseEntity<?> getGenerationByDate(Authentication authentication, @PathVariable LocalDate date) {
        String userName = authentication.getName();
        log.info("Received request to get power generation by date {} for user: {}", date, userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyByDay(userName, date));
    }

    @GetMapping("/generation/stats")
    public ResponseEntity<?> getGenerationStats(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to get generation stats for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.getGenerationStats(userName));
    }
}
