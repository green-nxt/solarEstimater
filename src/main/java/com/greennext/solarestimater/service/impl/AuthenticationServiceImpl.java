package com.greennext.solarestimater.service.impl;

import com.greennext.solarestimater.model.CustomerCredentials;
import com.greennext.solarestimater.repository.CustomerCredentialsRepository;
import com.greennext.solarestimater.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private CustomerCredentialsRepository customerCredentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean authenticate(String username, String password) {
        Optional<CustomerCredentials> credentials = customerCredentialsRepository.findByUsername(username);
        if (credentials.isPresent() && credentials.get().isActive()) {
            return passwordEncoder.matches(password, credentials.get().getPassword());
        }
        return false;
    }

    @Override
    @Transactional
    public void updateLastLogin(String username) {
        customerCredentialsRepository.updateLastLoginDate(username, LocalDateTime.now());
    }

    @Override
    @Transactional
    public boolean registerCustomer(CustomerCredentials credentials) {
        if (customerCredentialsRepository.existsByUsername(credentials.getUsername())) {
            return false;
        }
        // Encode password before saving
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        credentials.setActive(true);
        credentials.setLastLoginDate(LocalDateTime.now());
        customerCredentialsRepository.save(credentials);
        return true;
    }

    @Override
    @Transactional
    public void updateActiveStatus(String username, boolean status) {
        customerCredentialsRepository.updateActiveStatus(username, status);
    }

    @Override
    public boolean isActiveUser(String username) {
        Optional<CustomerCredentials> credentials = customerCredentialsRepository.findByUsername(username);
        return credentials.map(CustomerCredentials::isActive).orElse(false);
    }

    @Override
    public CustomerCredentials findByUsername(String username) {
        return customerCredentialsRepository.findByUsername(username).orElse(null);
    }
}
