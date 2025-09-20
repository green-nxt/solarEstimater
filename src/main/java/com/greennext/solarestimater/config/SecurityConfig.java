//package com.greennext.solarestimater.config;
//
//import com.greennext.solarestimater.service.impl.UserAuthenticationServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    UserAuthenticationServiceImpl userAuthenticationService;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
/// /        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
////    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder)
////            throws Exception {
////        return http.getSharedObject(AuthenticationManagerBuilder.class)
////                .userDetailsService(userAuthenticationService)
////                .passwordEncoder(passwordEncoder)
////                .and()
////                .build();
////    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/login").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Optional, for stateless JWT etc.
//                .userDetailsService(userAuthenticationService)
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authBuilder.userDetailsService(userAuthenticationService).passwordEncoder(this.passwordEncoder());
//        return authBuilder.build();
//    }
//}
