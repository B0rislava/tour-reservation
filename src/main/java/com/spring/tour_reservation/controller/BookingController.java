package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.model.User;
import com.spring.tour_reservation.repository.UserRepository;
import com.spring.tour_reservation.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    @GetMapping
    public String listBookings(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("bookings", bookingService.getBookingsForUser(user.getId()));
        return "bookings";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam Long tourId, 
                                @RequestParam int participants,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        
        try {
            bookingService.bookTour(user.getId(), tourId, participants);
            redirectAttributes.addAttribute("success", "");
            return "redirect:/bookings";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/tours/" + tourId;
        }
    }

    @PostMapping("/cancel")
    public String cancelBooking(@RequestParam Long bookingId,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();

        try {
            bookingService.cancelBooking(bookingId, user.getId());
            redirectAttributes.addAttribute("cancelled", "true");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }

        return "redirect:/bookings";
    }
}
