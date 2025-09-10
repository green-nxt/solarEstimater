package com.greennext.solarestimater.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.greennext.solarestimater.model.SolarPlant;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllPlantsInfoResponseBody implements WebClientResponseBody{
    @JsonAlias("err")
    private int errorCode;
    @JsonAlias("desc")
    private String description;
    @JsonAlias("dat")
    private AllPlantsResponseBody data;
}
