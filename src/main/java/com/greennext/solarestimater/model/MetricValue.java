package com.greennext.solarestimater.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MetricValue {
    boolean realtime;
    @JsonAlias("field")
    List<String> metricField;
}
