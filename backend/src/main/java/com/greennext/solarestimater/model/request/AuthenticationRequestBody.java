package com.greennext.solarestimater.model.request;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthenticationRequestBody {

    private String usr;
    private String pwd;
    private String companyKey;
}
