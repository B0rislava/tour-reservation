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
}
