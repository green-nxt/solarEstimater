package com.greennext.solarestimater.repository;

import com.greennext.solarestimater.model.Inverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InverterRepository extends JpaRepository<Inverter, String> {
    Optional<Inverter> findBySerialNumber(String serialNumber);
    Optional<Inverter> findByPlantNumber(String plantNumber);
    boolean existsBySerialNumber(String serialNumber);
}
