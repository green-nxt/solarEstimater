package com.greennext.solarestimater.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class SolarPlant {

    @Id
    private int pid;
    private int uid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    private Customer customer;

    private String name;
    private int status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "profit_id", referencedColumnName = "id")
    private Profit profit;
    private String nominalPower;
    private String energyYearEstimate;
    private String designCompany;
    private String picBig;
    private String picSmall;
    private String install;
    private String gts;

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "inverter_serial_number", referencedColumnName = "serialNumber")
//    private Inverter inverter;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DailyEnergyGeneration> dailyGenerations = new ArrayList<>();

}
