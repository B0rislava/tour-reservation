package com.spring.tour_reservation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class LoginController {

    @GetMapping("/api/v1/auth/status")
    public Map<String, String> status() {
        return Map.of("status", "API is running");
    }
}
