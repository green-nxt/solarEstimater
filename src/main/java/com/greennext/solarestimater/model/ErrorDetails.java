package com.greennext.solarestimater.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class ErrorDetails {
    private boolean isSuccess;
    private int code;
    private String message;
}
