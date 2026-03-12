package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public String listBookings(Model model) {
        // Hardcoded user ID 2 for Maria Georgieva
        Long userId = 2L;
        model.addAttribute("bookings", bookingService.getBookingsForUser(userId));
        return "bookings";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam Long tourId, 
                                @RequestParam int participants,
                                RedirectAttributes redirectAttributes) {
        // should be authenticated user
        Long userId = 2L; 
        
        try {
            bookingService.bookTour(userId, tourId, participants);
            redirectAttributes.addAttribute("success", "");
            return "redirect:/bookings";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/tours/" + tourId;
        }
    }
}
