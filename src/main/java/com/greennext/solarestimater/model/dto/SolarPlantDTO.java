package com.greennext.solarestimater.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolarPlantDTO {
    private int pid;
    private int uid;
    private String name;
    private int status;
    private AddressDTO address;
    private String nominalPower;
    private String energyYearEstimate;
    private String designCompany;
    private String picBig;
    private String picSmall;
    private String install;
    private String gts;
}
