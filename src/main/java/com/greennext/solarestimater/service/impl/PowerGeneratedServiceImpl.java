package com.greennext.solarestimater.service.impl;

import com.greennext.solarestimater.model.Customer;
import com.greennext.solarestimater.model.Inverter;
import com.greennext.solarestimater.model.response.AuthenticationResponse;
import com.greennext.solarestimater.model.response.DailyGenerationResponseBody;
import com.greennext.solarestimater.model.response.DailyMetricResponseBody;
import com.greennext.solarestimater.repository.CustomerRepository;
import com.greennext.solarestimater.repository.InverterRepository;
import com.greennext.solarestimater.service.PowerGeneratedService;
import com.greennext.solarestimater.util.CryptoUtil;
import com.greennext.solarestimater.util.PropertiesUtil;
import com.greennext.solarestimater.webclient.GreenNxtWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.greennext.solarestimater.util.AppConstants.*;
import static com.greennext.solarestimater.util.CryptoUtil.sha1ToLowerCase;

@Service
public class PowerGeneratedServiceImpl implements PowerGeneratedService {

    @Autowired private PropertiesUtil property;
    @Autowired private GreenNxtWebClient greenNxtWebClient;
    @Autowired private InverterRepository inverterRepository;
    @Autowired private CustomerRepository customerRepository;

    private Customer validateAndGetCustomer(String userId) {
        return customerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    private Inverter validateAndGetInverter(Customer customer) {
        Inverter inverter = customer.getInverter();
        if (inverter == null) {
            throw new RuntimeException("No inverter found for customer");
        }
        return inverter;
    }

    private String buildDeviceDataAction(Inverter inverter, LocalDate date) {
        return PARAM_PREFIX + ACTION + EQUALS + ACTION_QUERY_DEVICE_DATA +
                PARAM_PREFIX + LANGUAGE + EQUALS + DEFAULT_LANGUAGE +
                PARAM_PREFIX + PLANT_NUMBER + EQUALS + inverter.getPlantNumber() +
                PARAM_PREFIX + DEVICE_CODE + EQUALS + inverter.getDeviceCode() +
                PARAM_PREFIX + DEVICE_ADDRESS + EQUALS + inverter.getDeviceAddress() +
                PARAM_PREFIX + SERIAL_NUMBER + EQUALS + inverter.getSerialNumber() +
                PARAM_PREFIX + DATE + EQUALS + date;
    }

    private String generateSign(String salt, Customer customer, String action) {
        return sha1ToLowerCase((salt + customer.getSecret() + customer.getToken() + action).getBytes());
    }

    private Map<String, String> createQueryParams(String sign, String salt, String token, String action) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(SIGN, sign);
        queryParams.put(SALT, salt);
        queryParams.put(TOKEN, token);
        queryParams.put(ACTION, action);
        return queryParams;
    }

    @Override
    public DailyGenerationResponseBody getPowerGeneratedByDate(String userId, LocalDate date) {
        try {
            // Validate and get customer and inverter
            Customer customer = validateAndGetCustomer(userId);
            Inverter inverter = validateAndGetInverter(customer);

            // Generate salt and build action string
            String salt = System.currentTimeMillis() + "";
            String action = buildDeviceDataAction(inverter, date);

            // Generate sign and create query parameters
            String sign = generateSign(salt, customer, action);
            Map<String, String> queryParams = createQueryParams(sign, salt, customer.getToken(), action);

            // Make API call using WebClient
            DailyGenerationResponseBody responseBody = new DailyGenerationResponseBody();
            return (DailyGenerationResponseBody) greenNxtWebClient.get(
                    property.getInverterUrl(),
                    responseBody,
                    queryParams
            );
        } catch (Exception e) {
            DailyGenerationResponseBody errorResponse = new DailyGenerationResponseBody();
            errorResponse.setErrorCode(1);
            errorResponse.setDescription("Failed to fetch power generation data: " + e.getMessage());
            return errorResponse;
        }
    }

    @Override
    public AuthenticationResponse authenticateUser(String username, String password) {
        try {
            String salt = System.currentTimeMillis() + "";
            String sha1Password = CryptoUtil.sha1ToLowerCase(password.getBytes());

            String action = PARAM_PREFIX + ACTION + EQUALS + ACTION_AUTH +
                    PARAM_PREFIX + USER + EQUALS + username +
                    PARAM_PREFIX + COMPANY_KEY + EQUALS + property.getCompanyKey();

            String sign = CryptoUtil.sha1ToLowerCase((salt + sha1Password + action).getBytes());

            // Only include sign, salt, and action in query params
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put(SIGN, sign);
            queryParams.put(SALT, salt);
            queryParams.put(ACTION, action);

            AuthenticationResponse responseBody = new AuthenticationResponse();
            AuthenticationResponse response = (AuthenticationResponse) greenNxtWebClient.get(
                    property.getInverterUrl(),
                    responseBody,
                    queryParams
            );

            // If authentication was successful, update customer details in database
            if (response != null && response.getErrorCode() == 0 && response.getData() != null) {
                Customer customer = customerRepository.findByUserId(username)
                        .orElse(new Customer()); // Create new customer if not exists

                // Update customer details
                customer.setUserId(username);
                customer.setSecret(response.getData().getSecret());
                customer.setToken(response.getData().getToken());
                customer.setTokenDuration(String.valueOf(response.getData().getExpire()));

                // Save to database
                customerRepository.save(customer);
            }

            return response;
        } catch (Exception e) {
            AuthenticationResponse errorResponse = new AuthenticationResponse();
            errorResponse.setErrorCode(1);
            errorResponse.setDescription("Authentication failed: " + e.getMessage());
            return errorResponse;
        }
    }
}
