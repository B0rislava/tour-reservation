package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.dto.TourDto;
import com.spring.tour_reservation.dto.UserDto;
import com.spring.tour_reservation.service.TourService;
import com.spring.tour_reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final TourService tourService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, Principal principal) {
        userService.deleteUserById(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tours")
    public ResponseEntity<List<TourDto>> getAllTours() {
        return ResponseEntity.ok(tourService.getAllTours());
    }

    @DeleteMapping("/tours/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id, Principal principal) {
        tourService.deleteTour(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
