package com.greennext.solarestimater.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Customer {

    @Id
    @Column(unique = true, nullable = false)
    private String userId;
    private String password;
    private String name;

    @Column(length = 64)  // Token length from API
    private String token;

    @Column(name = "token_duration")
    private String tokenDuration;

    @Column(length = 64)  // SHA-1 hash is 40 chars, giving some buffer
    private String secret;


    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "inverter_serial_number", referencedColumnName = "serialNumber", nullable = false)
    @JsonManagedReference("customer-inverter")
    private Inverter inverter;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonManagedReference("customer-powerrecords")
    private List<DailyPowerRecord> powerRecords = new ArrayList<>();
}
