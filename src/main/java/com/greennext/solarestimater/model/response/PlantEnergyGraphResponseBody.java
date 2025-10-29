package com.greennext.solarestimater.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class PlantEnergyGraphResponseBody extends PlantEnergyGenerationResponseBody {
    @JsonProperty("dat")
    private Map<String, Object> dataWrapper;


    @SuppressWarnings("unchecked") // Suppress warning as we are checking with instanceof
    public List<Map<String, String>> extractDataPoints() {
        if (dataWrapper == null) {
            return new ArrayList<>();
        }
        String[] dataKeys = {"outputPower", "perday", "permonth"};
        for (String key : dataKeys) {
            if (dataWrapper.containsKey(key)) {
                Object value = dataWrapper.get(key);
                if (value instanceof List) {
                    return (List<Map<String, String>>) value;
                }
            }
        }
        return new ArrayList<>();
    }
}
