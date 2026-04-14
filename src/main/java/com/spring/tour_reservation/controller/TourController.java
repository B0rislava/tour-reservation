package com.spring.tour_reservation.controller;

import com.spring.tour_reservation.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.spring.tour_reservation.dto.TourDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/tours")
@RequiredArgsConstructor
@Tag(name = "Tours", description = "Operations related to viewing tours")
public class TourController {

    private final TourService tourService;

    @GetMapping
    @Operation(summary = "Get all available tours")
    public ResponseEntity<List<TourDto>> getAllTours() {
        return ResponseEntity.ok(tourService.getAllTours());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific tour details by ID")
    public ResponseEntity<TourDto> getTourDetails(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.getTourById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search tours by location")
    public ResponseEntity<List<TourDto>> searchTours(@RequestParam String location) {
        return ResponseEntity.ok(tourService.searchToursByLocation(location));
    }

    @GetMapping("/guide/me")
    @Operation(summary = "Get tours created by the authenticated guide")
    public ResponseEntity<List<TourDto>> getMyTours(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(tourService.getToursByGuideEmail(principal.getName()));
    }

    @PostMapping("/guide/me")
    @Operation(summary = "Create a new tour (Guides only)")
    public ResponseEntity<Long> createTour(@Valid @RequestBody TourDto tourDto, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(tourService.createTour(tourDto, principal.getName()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tour (Guides/Admin only)")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        tourService.deleteTour(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a tour (Guides only)")
    public ResponseEntity<Void> updateTour(@PathVariable Long id, @Valid @RequestBody TourDto tourDto, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        tourService.updateTour(id, tourDto, principal.getName());
        return ResponseEntity.ok().build();
    }
}
