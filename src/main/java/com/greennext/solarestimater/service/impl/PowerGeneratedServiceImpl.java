package com.greennext.solarestimater.service.impl;

import com.greennext.solarestimater.Exception.SolarEstimatorException;
import com.greennext.solarestimater.model.*;
import com.greennext.solarestimater.model.dto.DailyEnergyDTO;
import com.greennext.solarestimater.model.dto.GraphDataPointDTO;
import com.greennext.solarestimater.model.dto.SolarPlantDTO;
import com.greennext.solarestimater.model.mapper.ResponseBodyMapper;
import com.greennext.solarestimater.model.response.*;
import com.greennext.solarestimater.repository.*;
import com.greennext.solarestimater.service.PowerGeneratedService;
import com.greennext.solarestimater.util.CryptoUtil;
import com.greennext.solarestimater.util.PropertiesUtil;
import com.greennext.solarestimater.webclient.GreenNxtWebClient;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.greennext.solarestimater.util.AppConstants.*;
import static com.greennext.solarestimater.util.CryptoUtil.sha1ToLowerCase;

@Service
@Slf4j
public class PowerGeneratedServiceImpl implements PowerGeneratedService {

    @Autowired PropertiesUtil property;
    @Autowired GreenNxtWebClient greenNxtWebClient;
    @Autowired InverterRepository inverterRepository;
    @Autowired CustomerRepository customerRepository;
    @Autowired ResponseBodyMapper ResponseBodyMapper;
    @Autowired SolarPlantRepository solarPlantRepository;
    @Autowired DailyEnergyGenerationRepository dailyEnergyGenerationRepository;
    @Autowired
    CustomerCredentialsRepository customerCredentialsRepository;

    private Customer validateAndGetCustomer(String userId) {
        Customer customer = customerCredentialsRepository.findByUsername(userId)
                .orElseThrow(() -> new RuntimeException("Customer credentials not found")).getCustomer();
        return customerRepository.findByUserId(customer.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    private Inverter validateAndGetInverter(Customer customer) {
//        Inverter inverter = customer.getPlants().getFirst().getInverter();
//        if (inverter == null) {
//            throw new RuntimeException("No inverter found for customer");
//        }
//        return inverter;
        return null;
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
        queryParams.put(TOKEN, token + action);
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
            username = username.replaceAll("\\s", "%20");
            String sha1Password = CryptoUtil.sha1ToLowerCase(password.getBytes());

            String action = PARAM_PREFIX + ACTION + EQUALS + ACTION_AUTH +
                    PARAM_PREFIX + USER + EQUALS + username +
                    PARAM_PREFIX + COMPANY_KEY + EQUALS + property.getCompanyKey();

            String sign = CryptoUtil.sha1ToLowerCase((salt + sha1Password + action).getBytes());
            log.info("Action for authentication: {}", action);
            action = action.replace("%20", " ");
            // Only include sign, salt, and action in query params
            Map<String, String> queryParams = new LinkedHashMap<>();
            queryParams.put(SIGN, sign);
            queryParams.put(SALT, salt + action);

            AuthenticationResponse responseBody = new AuthenticationResponse();
            AuthenticationResponse response = (AuthenticationResponse) greenNxtWebClient.get(
                    property.getInverterUrl(),
                    responseBody,
                    queryParams
            );

            // If authentication was successful, update customer details in database
            if (response != null && response.getErrorCode() == 0 && response.getData() != null) {
                username = username.replace("%20", " ");
                Customer customer = customerRepository.findByUserId(username)
                        .orElse(new Customer());

                // Update customer details
                customer.setUserId(username);
                customer.setSecret(response.getData().getSecret());
                customer.setToken(response.getData().getToken());
                customer.setTokenDuration(String.valueOf(response.getData().getExpire()));

                // Save to database
                customerRepository.save(customer);
            } else {
                log.info("Error with message {}", response);
                throw new SolarEstimatorException(
                        new ErrorDetails(false, response.getErrorCode(), response.getDescription()));
            }
            return response;
        } catch (SolarEstimatorException e) {
            log.error("Authentication error: {}", e.getErrorDetails().getMessage());
            throw new SolarEstimatorException(e.getErrorDetails());
        } catch (Exception e) {
            log.error("Unexpected error during authentication: {}", e.getMessage());
            ErrorDetails error = new ErrorDetails(false, 1, "Authentication failed: " + e.getMessage());
            throw new SolarEstimatorException(error);
        }
    }

    @Transactional
    public ResponseEntity<?> queryAllPlants(String userId) {
        try {
            Customer customer = validateAndGetCustomer(userId);
            SolarPlant solarPlant = solarPlantRepository.findByCustomer(customer).orElse(null);

            String salt = System.currentTimeMillis() + "";
            String action = PARAM_PREFIX + ACTION + EQUALS + ACTION_QUERY_PLANTS;

            // Generate sign and create query parameters
            String sign = generateSign(salt, customer, action);
            Map<String, String> queryParams = createQueryParams(sign, salt, customer.getToken(), action);
            AllPlantsInfoResponseBody responseBody = new AllPlantsInfoResponseBody();
            responseBody = (AllPlantsInfoResponseBody) greenNxtWebClient.get(
                    property.getInverterUrl(),
                    responseBody,
                    queryParams
            );
            if (responseBody != null && responseBody.getErrorCode() == 0 && responseBody.getData() != null) {
                List<SolarPlantDTO> solarPlants = ResponseBodyMapper.mapToPlantDTOList(responseBody);
                log.info("Plant info response: {}",solarPlants.getFirst());
                if (solarPlant==null) {
                    List<SolarPlant> plants = responseBody.getData().getPlant();
                    plants = plants.stream().peek(p -> p.setCustomer(customer)).toList();
                    solarPlantRepository.saveAll(plants);
                }
                return new ResponseEntity<>(solarPlants, HttpStatus.OK);
            } else {
                ErrorDetails error = new ErrorDetails(false, responseBody.getErrorCode(),
                        responseBody.getDescription());
                throw new SolarEstimatorException(error);
            }

        } catch (SolarEstimatorException e) {
            log.error("Error fetching plant data: {}", e.getErrorDetails().getMessage());
            throw new SolarEstimatorException(e.getErrorDetails());
        } catch (Exception e) {
            log.error("Unexpected error while fetching all plants data: {}", e.getMessage());
            ErrorDetails error = new ErrorDetails(false, 1, "Failed to fetch plant data: " + e.getMessage());
            throw new SolarEstimatorException(error);
        }
    }
    
    public ResponseEntity<?> getEnergyByDay(String userId, LocalDate date) {
        try {
            Customer customer = validateAndGetCustomer(userId);
            String salt = System.currentTimeMillis() + "";
            SolarPlant plant = customer.getPlants().getFirst();
            String plantId = String.valueOf(plant.getPid());

            date = date != null ? date : LocalDate.now();

            String action = PARAM_PREFIX + ACTION + EQUALS + ACTION_QUERY_PLANT_ENERGY_DAY +
                    PARAM_PREFIX + PLANT_ID + EQUALS + plantId + PARAM_PREFIX + DATE + EQUALS + date;
            String sign = generateSign(salt, customer, action);
            Map<String, String> queryParams = createQueryParams(sign, salt, customer.getToken(), action);

            PlantEnergyGenerationResponseBody responseBody = new PlantEnergyGenerationResponseBody();
            responseBody = (PlantEnergyGenerationResponseBody) greenNxtWebClient.get(
                    property.getInverterUrl(),
                    responseBody,
                    queryParams
            );
            if (responseBody != null && responseBody.getErrorCode() == 0
                    && responseBody.getEnergyData() != null) {
                DailyEnergyDTO dailyEnergyDto = ResponseBodyMapper.mapDailyEnergyDto(responseBody);
                log.info("Energy generated today: {}",dailyEnergyDto);
                persistDailyGenerationData(date, plant, dailyEnergyDto);
                return new ResponseEntity<>(dailyEnergyDto, HttpStatus.OK);
            } else {
                ErrorDetails error = new ErrorDetails(false, responseBody.getErrorCode(),
                        responseBody.getDescription());
                throw new SolarEstimatorException(error);
            }
        } catch (SolarEstimatorException e) {
            log.error("Error fetching energy data: {}", e.getMessage());
            throw new SolarEstimatorException(e.getErrorDetails());
        } catch (Exception e) {
            log.error("Unexpected error fetching energy data: {}", e.getMessage());
            ErrorDetails error = new ErrorDetails(false, 1, "Failed to fetch energy data: " + e.getMessage());
            throw new SolarEstimatorException(error);
        }
    }

    @Override
    public ResponseEntity<?> getEnergyDaily(String userId) {
        return getEnergyByDay(userId, null);
    }

    public ResponseEntity<?> getEnergyByMonth(String userId, LocalDate date) {
        try {
            Customer customer = validateAndGetCustomer(userId);
            String salt = System.currentTimeMillis() + "";
            SolarPlant plant = customer.getPlants().getFirst();
            String plantId = String.valueOf(plant.getPid());
            date = date != null ? date : LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String month = date.format(formatter);

            String action = PARAM_PREFIX + ACTION + EQUALS + ACTION_QUERY_PLANT_ENERGY_MONTH +
                    PARAM_PREFIX + PLANT_ID + EQUALS + plantId + PARAM_PREFIX + DATE + EQUALS + month;

            String sign = generateSign(salt, customer, action);
            Map<String, String> queryParams = createQueryParams(sign, salt, customer.getToken(), action);

            PlantEnergyGenerationResponseBody responseBody = new PlantEnergyGenerationResponseBody();
            responseBody = (PlantEnergyGenerationResponseBody) greenNxtWebClient.get(
                    property.getInverterUrl(),
                    responseBody,
                    queryParams
            );
            if (responseBody != null && responseBody.getErrorCode() == 0
                    && responseBody.getEnergyData() != null) {
                DailyEnergyDTO dailyEnergyDto = ResponseBodyMapper.mapDailyEnergyDto(responseBody);
                log.info("Energy generated Monthly: {}", dailyEnergyDto);
//                persistDailyGenerationData(date, plant, dailyEnergyDto);
                return new ResponseEntity<>(dailyEnergyDto, HttpStatus.OK);
            } else {
                ErrorDetails error = new ErrorDetails(false, responseBody.getErrorCode(),
                        responseBody.getDescription());
                throw new SolarEstimatorException(error);
            }
        } catch (SolarEstimatorException e) {
            log.error("Error fetching monthly energy data: {}", e.getMessage());
            throw new SolarEstimatorException(e.getErrorDetails());
        } catch (Exception e) {
            log.error("Unexpected error fetching monthly energy data: {}", e.getMessage());
            ErrorDetails error = new ErrorDetails(false, 1, "Failed to fetch energy data: " + e.getMessage());
            throw new SolarEstimatorException(error);
        }
    }

    @Override
    public ResponseEntity<?> getEnergyMonthly(String userId) {
        return getEnergyByMonth(userId, null);
    }

    @Override
    public ResponseEntity<?> getGenerationStats(String userId) {
        Customer customer = validateAndGetCustomer(userId);
        SolarPlant solarPlant = customer.getPlants().getFirst();

        float plantCapacity = Float.parseFloat(solarPlant.getNominalPower());
        double generationToday = solarPlant.getDailyGenerations().stream()
                .filter(d -> d.getDate().equals(LocalDate.now()))
                .map(DailyEnergyGeneration::getEnergyGenerated)
                .findFirst().orElse(0.0);
        float discount = customer.getDiscount();
        PlantGenerationStats response = getPlantGenerationStats(generationToday, discount, plantCapacity);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getGenerationGraphData(String userId, String type, String date) {
        try {
            // 1. Validate the user and get customer details
            Customer customer = validateAndGetCustomer(userId);
            SolarPlant plant = customer.getPlants().getFirst();
            String plantId = String.valueOf(plant.getPid());

            // 2. Prepare parameters for the API call based on the requested type
            String salt = System.currentTimeMillis() + "";
            String actionName;
            String dateValue;
            DateTimeFormatter formatter;

            switch (type.toLowerCase()) {
                case "day":
                    actionName = ACTION_QUERY_PLANT_ACTIVE_POWER_ONE_DAY;
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    dateValue = (date != null) ? LocalDate.parse(date, formatter).format(formatter) : LocalDate.now().format(formatter);
                    break;

                case "month":
                    actionName = ACTION_QUERY_PLANT_ENERGY_MONTH_PER_DAY;
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                    dateValue = (date != null) ? YearMonth.parse(date, formatter).format(formatter) : YearMonth.now().format(formatter);
                    break;

                case "year":
                    actionName = ACTION_QUERY_PLANT_ENERGY_YEAR_PER_MONTH;
                    formatter = DateTimeFormatter.ofPattern("yyyy");
                    dateValue = (date != null) ? Year.parse(date, formatter).format(formatter) : Year.now().format(formatter);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid graph type specified. Allowed values are 'day', 'month', 'year'.");
            }

            // 3. Build the dynamic action string
            String action = PARAM_PREFIX + ACTION + EQUALS + actionName +
                    PARAM_PREFIX + PLANT_ID + EQUALS + plantId +
                    PARAM_PREFIX + DATE + EQUALS + dateValue;

            // 4. Generate the signature and query parameters
            String sign = generateSign(salt, customer, action);
            Map<String, String> queryParams = createQueryParams(sign, salt, customer.getToken(), action);

            // 5. Make the API call
            PlantEnergyGraphResponseBody responseBody = new PlantEnergyGraphResponseBody();
            responseBody = (PlantEnergyGraphResponseBody) greenNxtWebClient.get(
                    property.getInverterUrl(),
                    responseBody,
                    queryParams
            );

            // 6. Process the response
            if (responseBody != null && responseBody.getErrorCode() == 0 && responseBody.getDataWrapper() != null) {
                List<GraphDataPointDTO> graphData = ResponseBodyMapper.mapToGraphDataDTO(responseBody);
                log.info("Successfully fetched graph data for type '{}' and date '{}'", type, dateValue);
                return new ResponseEntity<>(graphData, HttpStatus.OK);
            } else {
                ErrorDetails error = new ErrorDetails(false, responseBody.getErrorCode(), responseBody.getDescription());
                throw new SolarEstimatorException(error);
            }
        } catch (IllegalArgumentException | DateTimeParseException e) {
            log.error("Invalid parameter for generation graph data: {}", e.getMessage());
            throw new SolarEstimatorException(new ErrorDetails(false, 400, e.getMessage()));
        } catch (SolarEstimatorException e) {
            log.error("Error fetching generation graph data: {}", e.getMessage());
            throw new SolarEstimatorException(e.getErrorDetails());
        } catch (Exception e) {
            log.error("Unexpected error fetching generation graph data: {}", e.getMessage());
            ErrorDetails error = new ErrorDetails(false, 500, "Failed to fetch graph data: " + e.getMessage());
            throw new SolarEstimatorException(error);
        }
    }

    private static PlantGenerationStats getPlantGenerationStats(double generationToday, float discount, float plantCapacity) {
        float savingsToday = (float) ((generationToday * 11) * discount);
        float generationMonthly = 0.0f;
        float totalSavings = generationMonthly * 11 * discount;
        float environmentalImpact = plantCapacity * 0.8f;
        float treeEquivalent = plantCapacity * (13.27f / 365);

        PlantGenerationStats response = new PlantGenerationStats();
        response.setCapacity(plantCapacity);
        response.setGenerationMonthly(generationMonthly);
        response.setDiscount(discount);
        response.setSavingsToday(savingsToday);
        response.setTreeImpact(treeEquivalent);
        response.setSavingsToday(totalSavings);
        response.setEnvironmentalImpact(environmentalImpact);
        return response;
    }

    @Transactional
    protected void persistDailyGenerationData(LocalDate date, SolarPlant plant, DailyEnergyDTO dailyEnergyDto) {
        DailyEnergyGeneration dailyEnergyGeneration = dailyEnergyGenerationRepository.findByPlantAndDate(plant, date);
        if(dailyEnergyGeneration != null) {
            log.info("Updating existing daily energy record for date: {}", date);
            dailyEnergyGeneration.setEnergyGenerated(Double.parseDouble(dailyEnergyDto.energy));
            dailyEnergyGenerationRepository.save(dailyEnergyGeneration);
        } else {
            log.info("Creating new daily energy record for date: {}", date);
            dailyEnergyGeneration = new DailyEnergyGeneration();
            dailyEnergyGeneration.setDate(date);
            dailyEnergyGeneration.setPlant(plant);
            dailyEnergyGeneration.setEnergyGenerated(Double.parseDouble(dailyEnergyDto.energy));
            dailyEnergyGenerationRepository.save(dailyEnergyGeneration);
        }
    }
}
