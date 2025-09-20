package com.greennext.solarestimater.model.request;

import lombok.Data;

@Data
public class CustomerLoginRequestBody {
    private String username;
    private String password;
}
