package com.greennext.solarestimater.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDate;

@Entity
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailyPowerRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @Column(nullable = false, columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Column(nullable = false)
    private Double totalPowerGenerated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inverter_serial_number", referencedColumnName = "serialNumber", nullable = false)
    @JsonBackReference("inverter-powerrecords")
    private Inverter inverter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "userId", nullable = false)
    @JsonBackReference("customer-powerrecords")
    private Customer customer;
}

