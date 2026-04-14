package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.dto.BookingDto;
import com.spring.tour_reservation.model.User;
import com.spring.tour_reservation.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.tour_reservation.dto.BookingRequestDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Operations related to tour bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    @Operation(summary = "List all bookings for the authenticated user")
    public ResponseEntity<List<BookingDto>> listBookings(Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).build();
        User user = bookingService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(bookingService.getBookingsForUser(user.getId()));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new booking for a tour")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequestDto request,
            Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).build();
        User user = bookingService.getUserByEmail(principal.getName());

        try {
            bookingService.bookTour(user.getId(), request.getTourId(), request.getParticipants());
            return ResponseEntity.ok(Map.of("message", "Booking confirmed successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cancel")
    @Operation(summary = "Cancel an existing booking")
    public ResponseEntity<?> cancelBooking(@RequestBody Map<String, Long> request,
            Principal principal) {
        if (principal == null)
            return ResponseEntity.status(401).build();
        User user = bookingService.getUserByEmail(principal.getName());
        Long bookingId = request.get("bookingId");

        try {
            bookingService.cancelBooking(bookingId, user.getId());
            return ResponseEntity.ok(Map.of("message", "Booking successfully cancelled."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
