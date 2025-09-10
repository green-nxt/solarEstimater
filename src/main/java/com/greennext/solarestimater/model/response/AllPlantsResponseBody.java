package com.greennext.solarestimater.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.greennext.solarestimater.model.SolarPlant;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllPlantsResponseBody {
    private int total;
    private int page;
    private int pagesize;
    private List<SolarPlant> plant;
}
