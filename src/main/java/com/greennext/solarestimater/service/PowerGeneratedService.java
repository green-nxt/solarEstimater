package com.greennext.solarestimater.service;

import com.greennext.solarestimater.model.response.AuthenticationResponse;
import com.greennext.solarestimater.model.response.DailyGenerationResponseBody;

import java.time.LocalDate;

public interface PowerGeneratedService {

    DailyGenerationResponseBody getPowerGeneratedByDate(String userId, LocalDate date);
    AuthenticationResponse authenticateUser(String username, String password);
}
