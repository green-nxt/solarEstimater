package com.greennext.solarestimater.service.impl;

import com.greennext.solarestimater.model.Customer;
import com.greennext.solarestimater.model.PowerGenerated;
import com.greennext.solarestimater.service.PowerGeneratedService;
import org.springframework.stereotype.Service;

@Service
public class PowerGeneratedServiceImpl implements PowerGeneratedService {
    @Override
    public String fetchPowerGenerated(PowerGenerated powerGenerated) {
        return "";
    }

    public void authenticateAccount(String userId, String password, String companyKey) {
        Customer customer = new Customer(userId, password, companyKey
        );
    }
}
