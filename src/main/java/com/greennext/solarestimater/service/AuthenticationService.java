package com.greennext.solarestimater.service;

import com.greennext.solarestimater.model.CustomerCredentials;

public interface AuthenticationService {
    boolean authenticate(String username, String password);
    void updateLastLogin(String username);
    boolean registerCustomer(CustomerCredentials credentials);
    void updateActiveStatus(String username, boolean status);
    boolean isActiveUser(String username);
    CustomerCredentials findByUsername(String username);
}
