package com.greennext.solarestimater.service;

import com.greennext.solarestimater.model.response.AuthenticationResponse;
import com.greennext.solarestimater.model.response.DailyGenerationResponseBody;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface PowerGeneratedService {

    DailyGenerationResponseBody getPowerGeneratedByDate(String userId, LocalDate date);
    AuthenticationResponse authenticateUser(String username, String password);
    ResponseEntity<?> queryAllPlants(String userId);
    ResponseEntity<?> getEnergyByDay(String userId, LocalDate date);
    ResponseEntity<?> getEnergyDaily(String userId);

    ResponseEntity<?> getEnergyMonthly(String userId);

    ResponseEntity<?> getEnergyByMonth(String userId, LocalDate date);
    ResponseEntity<?> getGenerationStats(String userId);

    ResponseEntity<?> getGenerationGraphData(String userName);

    ResponseEntity<?> getGenerationGraphData(String userName, String type, String date);
}
