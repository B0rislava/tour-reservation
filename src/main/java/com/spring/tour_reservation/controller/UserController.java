package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.dto.UserDto;
import com.spring.tour_reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        UserDto userDto = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", userDto);
        return "profile";
    }
}
