package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.dto.BookingDto;
import com.spring.tour_reservation.model.User;
import com.spring.tour_reservation.repository.UserRepository;
import com.spring.tour_reservation.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<BookingDto>> listBookings(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(bookingService.getBookingsForUser(user.getId()));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestParam Long tourId, 
                                           @RequestParam int participants,
                                           Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();                                    
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        
        try {
            bookingService.bookTour(user.getId(), tourId, participants);
            return ResponseEntity.ok(Map.of("message", "Booking confirmed successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelBooking(@RequestParam Long bookingId,
                                           Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();

        try {
            bookingService.cancelBooking(bookingId, user.getId());
            return ResponseEntity.ok(Map.of("message", "Booking successfully cancelled."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
