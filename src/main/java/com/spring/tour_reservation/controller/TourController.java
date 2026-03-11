package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tours", tourService.getAllTours());
        return "index";
    }

    @GetMapping("/tours/details")
    public String tourDetails() {
        return "tour-details";
    }

    @GetMapping("/bookings")
    public String bookings() {
        return "bookings";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
}
