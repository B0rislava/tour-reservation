package com.spring.tour_reservation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/v1/auth/**", "/api/v1/tours/**", "/css/**", "/js/**", "/images/**", "/h2-console/**", "/v3/api-docs/**", "/swagger-ui/**", "/*.html", "/").permitAll()
                    .requestMatchers("/api/v1/bookings/**", "/api/v1/users/**").hasAnyRole("TRAVELER", "GUIDE")
                    .anyRequest().authenticated()
                )
                .exceptionHandling(exc -> exc
                    .authenticationEntryPoint((req, res, authExc) -> res.setStatus(401))
                )
                .formLogin(form -> form
                    .loginProcessingUrl("/api/v1/auth/login")
                    .successHandler((req, res, auth) -> res.setStatus(200))
                    .failureHandler((req, res, exc) -> res.setStatus(401))
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutUrl("/api/v1/auth/logout")
                    .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))
                    .permitAll()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

            return http.build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure security filter chain", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        try {
            return config.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get authentication manager", e);
        }
    }
}
