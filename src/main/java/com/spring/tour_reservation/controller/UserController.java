package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.model.User;
import com.spring.tour_reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    public String profile(Model model) {
        // Hardcoded Maria Georgieva's ID
        User user = userRepository.findById(2L).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "profile";
    }
}
