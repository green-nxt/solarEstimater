package com.greennext.solarestimater.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.greennext.solarestimater.model.MetricTitle;
import com.greennext.solarestimater.model.MetricValue;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Getter @Setter
public class DailyMetricResponseBody implements WebClientResponseBody{

    @JsonAlias("title")
    List<MetricTitle> metricTitles;
    @JsonAlias("row")
    List<MetricValue> metricValues;
}
