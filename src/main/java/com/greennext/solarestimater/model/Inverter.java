package com.greennext.solarestimater.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Inverter {
    @Id
    @Column(unique = true, nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String companyKey;

    @Column(nullable = false)
    private String plantNumber;

    @Column(nullable = false)
    private String deviceCode;

    @Column(nullable = false)
    private String deviceAddress;

    @OneToMany(mappedBy = "inverter", fetch = FetchType.LAZY)
    @JsonManagedReference("inverter-powerrecords")
    private List<DailyPowerRecord> powerRecords = new ArrayList<>();

//    @OneToOne(mappedBy = "inverter", fetch = FetchType.LAZY)
//    private SolarPlant solarPlant;
}
