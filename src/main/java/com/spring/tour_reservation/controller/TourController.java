package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tours", tourService.getAllTours());
        return "index";
    }

    @GetMapping("/tours/{id}")
    public String tourDetails(@PathVariable Long id, Model model) {
        model.addAttribute("tour", tourService.getTourById(id));
        return "tour-details";
    }

}
