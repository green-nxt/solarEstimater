package com.greennext.solarestimater.controller;

import com.greennext.solarestimater.model.request.LoginRequestBody;
import com.greennext.solarestimater.model.response.AuthenticationResponse;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PowerGeneratedService powerGeneratedService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestBody request) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate token or return success response
            return ResponseEntity.ok("Login successful");

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/token/{userId}/{password}")
    public ResponseEntity<?> generateToken(@PathVariable String userId, @PathVariable String password) {
        try {
            AuthenticationResponse response = powerGeneratedService
                    .authenticateUser(userId, password);
            log.info("=========================> Response: {}", response.getData());
            // Generate token or return success response
            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}
