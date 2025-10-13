package com.greennext.solarestimater.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphDataPointDTO {
    private String timestamp;
    private Double value;
}