package com.greennext.solarestimater.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GraphData {
    @JsonProperty("perday")
    private List<PerDayData> perday;
}