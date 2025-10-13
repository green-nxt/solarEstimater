package com.greennext.solarestimater.controller;

import com.greennext.solarestimater.Exception.SolarEstimatorException;
import com.greennext.solarestimater.model.request.CustomerLoginRequestBody;
import com.greennext.solarestimater.model.request.LoginRequestBody;
import com.greennext.solarestimater.model.response.AuthenticationResponse;
import com.greennext.solarestimater.security.JwtUtil;
import com.greennext.solarestimater.service.PowerGeneratedService;
import com.greennext.solarestimater.service.ShineMonitorOpenApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    @Autowired
    PowerGeneratedService powerGeneratedService;
    @Autowired
    JwtUtil jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user and return JWT token")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successful authentication",
                            content = @Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials",
                            content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class)))
            }
    )
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
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/token/generate")
    @Operation(summary = "Generate Token", description = "Generate authentication token for user")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Token generated successfully",
                            content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthenticationResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))
                    )
            }
    )
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
