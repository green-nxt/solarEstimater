package com.greennext.solarestimater.repository;

import com.greennext.solarestimater.model.Customer;
import com.greennext.solarestimater.model.SolarPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolarPlantRepository extends JpaRepository<SolarPlant, String> {
    Optional<SolarPlant> findByCustomer(Customer customer);
}
