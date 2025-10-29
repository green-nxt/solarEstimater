package com.greennext.solarestimater.util;

public class AppConstants {
    // Query Parameter Keys
    public static final String SIGN = "sign";
    public static final String SALT = "salt";
    public static final String TOKEN = "token";
    public static final String ACTION = "action";
    public static final String USER = "usr";
    public static final String COMPANY_KEY = "company-key";
    public static final String PLANT_NUMBER = "pn";
    public static final String SERIAL_NUMBER = "sn";
    public static final String DEVICE_CODE = "devcode";
    public static final String DEVICE_ADDRESS = "devaddr";
    public static final String PLANT_ID = "plantid";

    public static final String DATE = "date";
    public static final String LANGUAGE = "i18n";

    // Action Values
    public static final String ACTION_AUTH = "auth";
    public static final String ACTION_QUERY_DEVICE_DATA = "queryDeviceDataOneDay";
    public static final String ACTION_QUERY_PLANTS = "queryPlants";
    public static final String ACTION_QUERY_PLANT_ENERGY_DAY = "queryPlantEnergyDay";
    public static final String ACTION_QUERY_PLANT_ENERGY_MONTH = "queryPlantEnergyMonth";

    // Other Constants
    public static final String DEFAULT_LANGUAGE = "zh_EN";
    public static final String PARAM_PREFIX = "&";
    public static final String EQUALS = "=";
    public static final String ACTION_QUERY_PLANT_ENERGY_MONTH_PER_DAY = "queryPlantEnergyMonthPerDay";
    public static final String ACTION_QUERY_PLANT_ACTIVE_POWER_ONE_DAY = "queryPlantActiveOuputPowerOneDay";
    public static final String ACTION_QUERY_PLANT_ENERGY_YEAR_PER_MONTH = "queryPlantEnergyYearPerMonth";
}
