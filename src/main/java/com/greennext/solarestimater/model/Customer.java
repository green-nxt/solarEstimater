package com.greennext.solarestimater.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Customer {

    @Id
    @Column(unique = true, nullable = false)
    private String userId;
    private String name;

    @Column(length = 64)
    private String token;

    @Column(name = "token_duration")
    private String tokenDuration;

    @Column(length = 64)
    private String secret;

    @Column(nullable = false)
    private float discount;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CustomerCredentials credentials;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SolarPlant> plants = new ArrayList<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonManagedReference("customer-powerrecords")
    private List<DailyPowerRecord> powerRecords = new ArrayList<>();
}
