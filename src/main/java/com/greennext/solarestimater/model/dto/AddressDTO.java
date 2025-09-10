package com.greennext.solarestimater.model.dto;


import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class AddressDTO {

    private String country;
    private String province;
    private String city;
    private String county;
    private String town;
    private String village;
    private String address;
    private String lon;
    private String lat;
    private int timezone;
}
