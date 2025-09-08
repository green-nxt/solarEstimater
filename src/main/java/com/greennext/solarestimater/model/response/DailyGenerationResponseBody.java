package com.greennext.solarestimater.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyGenerationResponseBody implements WebClientResponseBody{
    @JsonAlias("err")
    int errorCode;
    @JsonAlias("desc")
    String description;
    @JsonAlias("dat")
    DailyMetricResponseBody generationResponseData;

}
