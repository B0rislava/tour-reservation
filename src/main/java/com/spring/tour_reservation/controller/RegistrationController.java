package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.dto.RegistrationDto;
import com.spring.tour_reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") RegistrationDto registrationDto, 
                               RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(registrationDto);
            redirectAttributes.addFlashAttribute("registrationSuccess", "Registration successful! You can now log in.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("registrationError", e.getMessage());
            return "redirect:/signup";
        }
    }
}
