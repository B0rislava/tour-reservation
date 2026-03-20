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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operations related to user profiles and favorites")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get the authenticated user's profile details")
    public ResponseEntity<UserDto> profile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        UserDto userDto = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update the authenticated user's profile")
    public ResponseEntity<UserDto> updateProfile(@RequestBody UserUpdateRequestDto request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        UserDto updatedUser = userService.updateUserProfile(principal.getName(), request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/profile")
    @Operation(summary = "Delete the authenticated user's account")
    public ResponseEntity<Void> deleteProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        userService.deleteUser(principal.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/favorites/{tourId}")
    @Operation(summary = "Add a tour to user's favorites")
    public ResponseEntity<Void> addFavoriteTour(@PathVariable Long tourId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        userService.addSavedTour(principal.getName(), tourId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites/{tourId}")
    @Operation(summary = "Remove a tour from user's favorites")
    public ResponseEntity<Void> removeFavoriteTour(@PathVariable Long tourId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        userService.removeSavedTour(principal.getName(), tourId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    @Operation(summary = "Get all favorite tours for the user")
    public ResponseEntity<List<TourDto>> getFavoriteTours(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        List<TourDto> favoriteTours = userService.getSavedTours(principal.getName());
        return ResponseEntity.ok(favoriteTours);
    }
}
