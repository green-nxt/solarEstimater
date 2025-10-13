package com.greennext.solarestimater.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PerDayData {
    @JsonProperty("val")
    private String val;

    @JsonProperty("ts")
    private String ts;
}