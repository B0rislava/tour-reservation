package com.spring.tour_reservation.config;

import com.spring.tour_reservation.exception.TourReservationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            http
                    .csrf(csrf -> csrf
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .ignoringRequestMatchers("/api/v1/auth/login", "/api/v1/auth/signup")
                    )
                    .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/api/v1/auth/**",
                                    "/api/v1/tours/**",
                                    "/css/**",
                                    "/js/**",
                                    "/images/**",
                                    "/h2-console/**",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/*.html",
                                    "/",
                                    "/favicon.ico"
                            ).permitAll()
                            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                            .requestMatchers("/api/v1/bookings/**", "/api/v1/users/**")
                            .hasAnyRole("TRAVELER", "GUIDE", "ADMIN")
                            .anyRequest().authenticated()
                    )
                    .exceptionHandling(exc -> exc
                            .authenticationEntryPoint((req, res, authExc) -> res.setStatus(401))
                    )
                    .formLogin(form -> form
                            .loginProcessingUrl("/api/v1/auth/login")
                    .successHandler((req, res, auth) -> {
                        log.info("Successfully logged in user: {}", auth.getName());
                        res.setStatus(200);
                    })
                    .failureHandler((req, res, exc) -> {
                        log.warn("Login failed for user: {}. Reason: {}", req.getParameter("username"), exc.getMessage());
                        res.setStatus(401);
                    })
                            .permitAll()
                    )
                    .logout(logout -> logout
                            .logoutUrl("/api/v1/auth/logout")
                            .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))
                            .permitAll()
                    )
                    .headers(headers -> headers
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

            return http.build();
        } catch (Exception e) {
            throw new TourReservationException("Failed to configure security filter chain: " + e.getMessage());
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
            throw new TourReservationException("Failed to get authentication manager: " + e.getMessage());
        }
    }

    static class CsrfCookieFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain)
                throws ServletException, IOException {

            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            if (csrfToken != null) {
                csrfToken.getToken();
            }
            filterChain.doFilter(request, response);
        }
    }
}