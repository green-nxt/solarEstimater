package com.greennext.solarestimater.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Profit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String unitProfit;
    private String currency;
    private String currencyCountry;
    private String coal;
    private String co2;
    private String so2;
    @OneToOne(mappedBy = "profit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SolarPlant solarPlant;
}
