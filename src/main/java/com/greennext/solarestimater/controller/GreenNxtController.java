package com.greennext.solarestimater.controller;

import com.greennext.solarestimater.model.ErrorDetails;
import com.greennext.solarestimater.model.dto.DailyEnergyDTO;
import com.greennext.solarestimater.model.response.AllPlantsInfoResponseBody;
import com.greennext.solarestimater.model.response.PlantGenerationStats;
import com.greennext.solarestimater.service.PowerGeneratedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved plant details",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AllPlantsInfoResponseBody.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDetails.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDetails.class)))
            }
    )
    public ResponseEntity<?> getAllPlantsDetails(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to query all plants for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.queryAllPlants(userName));
    }

    @GetMapping("/generation/daily")
    @Operation(summary = "Get Daily Generation", description = "Retrieve daily power generation data for the authenticated user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved daily generation data",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyEnergyDTO.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access"),
                    @ApiResponse(responseCode = "403", description = "Forbidden access")
            }
    )
    public ResponseEntity<?> getDailyGeneration(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to query daily power generation for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyDaily(userName));
    }

    @GetMapping("/generation/monthly")
    @Operation(summary = "Get Monthly Generation", description = "Retrieve monthly power generation data for the authenticated user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved monthly generation data",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyEnergyDTO.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDetails.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDetails.class)))
            }
    )
    public ResponseEntity<?> getMonthlyGeneration(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to query monthly power generation for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyMonthly(userName));
    }

    @GetMapping("/generation/month/{date}")
    @Operation(summary = "Get Monthly Generation by Date", description = "Retrieve power generation data for a specific month for the authenticated user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved monthly generation data for the specified month",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyEnergyDTO.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDetails.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDetails.class)))
            }
    )
    public ResponseEntity<?> getMonthlyGeneration(Authentication authentication, @PathVariable LocalDate date) {
        String userName = authentication.getName();
        log.info("Received request to query power generation for month {} for user: {}", date, userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyByMonth(userName, date));
    }

    @GetMapping("/generation/date/{date}")
    @Operation(summary = "Get Generation by Date", description = "Retrieve power generation data for a specific date for the authenticated user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved generation data for the specified date",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyEnergyDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDetails.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDetails.class)))
            }
    )
    public ResponseEntity<?> getGenerationByDate(Authentication authentication, @PathVariable LocalDate date) {
        String userName = authentication.getName();
        log.info("Received request to get power generation by date {} for user: {}", date, userName);
        return ResponseEntity.ok(powerGeneratedService.getEnergyByDay(userName, date));
    }

    @GetMapping("/generation/stats")
    @Operation(summary = "Get Generation Stats", description = "Retrieve power generation statistics for the authenticated user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved generation statistics",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PlantGenerationStats.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDetails.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorDetails.class)))
            }
    )
    public ResponseEntity<?> getGenerationStats(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Received request to get generation stats for user: {}", userName);
        return ResponseEntity.ok(powerGeneratedService.getGenerationStats(userName));
    }
}
