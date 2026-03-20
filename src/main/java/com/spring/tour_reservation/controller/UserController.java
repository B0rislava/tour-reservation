package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.dto.UserDto;
import com.spring.tour_reservation.service.UserService;
import com.spring.tour_reservation.dto.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.spring.tour_reservation.dto.TourDto;

import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> profile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        UserDto userDto = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(@RequestBody UserUpdateRequestDto request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        UserDto updatedUser = userService.updateUserProfile(principal.getName(), request);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/favorites/{tourId}")
    public ResponseEntity<Void> addFavoriteTour(@PathVariable Long tourId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        userService.addSavedTour(principal.getName(), tourId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites/{tourId}")
    public ResponseEntity<Void> removeFavoriteTour(@PathVariable Long tourId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        userService.removeSavedTour(principal.getName(), tourId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<TourDto>> getFavoriteTours(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        List<TourDto> favoriteTours = userService.getSavedTours(principal.getName());
        return ResponseEntity.ok(favoriteTours);
    }
}
