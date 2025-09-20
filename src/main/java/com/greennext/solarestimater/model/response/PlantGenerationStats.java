package com.greennext.solarestimater.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantGenerationStats {

    private float capacity;
    private float discount;
    private float savingsToday;
    private float generationMonthly;
    private float totalSavings;
    private float environmentalImpact;
    private float treeImpact;

}
