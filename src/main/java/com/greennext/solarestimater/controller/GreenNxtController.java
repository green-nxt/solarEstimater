package com.greennext.solarestimater.controller;

import com.greennext.solarestimater.service.PowerGeneratedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "GreenNxt", description = "Endpoints for GreenNxt operations")
public class GreenNxtController {

    @Autowired
    PowerGeneratedService powerGeneratedService;

    @GetMapping("/plant/all")
    @Operation(summary = "Get All Plants Details", description = "Retrieve details of all plants associated with the authenticated user")
    public ResponseEntity<?> getAllPlantsDetails(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to query all plants for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.queryAllPlants(userName));
    }

    @GetMapping("/generation/daily")
    @Operation(summary = "Get Daily Generation", description = "Retrieve daily power generation data for the authenticated user")
    public ResponseEntity<?> getDailyGeneration(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to query daily power generation for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyDaily(userName));
    }

    @GetMapping("/generation/monthly")
    @Operation(summary = "Get Monthly Generation", description = "Retrieve monthly power generation data for the authenticated user")
    public ResponseEntity<?> getMonthlyGeneration(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to query monthly power generation for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyMonthly(userName));
    }

    @GetMapping("/generation/month/{date}")
    @Operation(summary = "Get Monthly Generation by Date", description = "Retrieve power generation data for a specific month for the authenticated user")
    public ResponseEntity<?> getMonthlyGeneration(Authentication authentication, @PathVariable LocalDate date) {
        String userName = authentication.getName();
        log.info("Received request to query power generation for month {} for user: {}", date, userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyByMonth(userName, date));
    }

    @GetMapping("/generation/date/{date}")
    @Operation(summary = "Get Generation by Date", description = "Retrieve power generation data for a specific date for the authenticated user")
    public ResponseEntity<?> getGenerationByDate(Authentication authentication, @PathVariable LocalDate date) {
        String userName = authentication.getName();
        log.info("Received request to get power generation by date {} for user: {}", date, userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyByDay(userName, date));
    }

    @GetMapping("/generation/stats")
    @Operation(summary = "Get Generation Stats", description = "Retrieve power generation statistics for the authenticated user")
    public ResponseEntity<?> getGenerationStats(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to get generation stats for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.getGenerationStats(userName));
    }
}
