package com.greennext.solarestimater.controller;

import com.greennext.solarestimater.Exception.SolarEstimatorException;
import com.greennext.solarestimater.model.request.CustomerLoginRequestBody;
import com.greennext.solarestimater.model.request.LoginRequestBody;
import com.greennext.solarestimater.model.response.AuthenticationResponse;
import com.greennext.solarestimater.security.JwtUtil;
import com.greennext.solarestimater.service.PowerGeneratedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    PowerGeneratedService powerGeneratedService;
    @Autowired
    JwtUtil jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestBody request) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate token or return success response
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(jwtUtils.generateToken(userDetails.getUsername()));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/token/generate")
    public ResponseEntity<?> generateToken(@RequestBody CustomerLoginRequestBody request) {
        try {
            AuthenticationResponse response = powerGeneratedService
                    .authenticateUser(request.getUsername(), request.getPassword());
            log.info("=========================> Response: {}", response.getData());
            // Generate token or return success response
            return ResponseEntity.ok("Login successful");
        } catch (SolarEstimatorException e) {
            throw new SolarEstimatorException(e.getErrorDetails());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
