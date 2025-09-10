package com.greennext.solarestimater.repository;

import com.greennext.solarestimater.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByUserId(String userId);
    Optional<Customer> findByToken(String token);
    boolean existsByUserId(String userId);
}
