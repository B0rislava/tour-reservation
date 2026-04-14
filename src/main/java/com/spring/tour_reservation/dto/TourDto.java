package com.spring.tour_reservation.dto;

import com.spring.tour_reservation.model.TourStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourDto {
    private Long id;
    private Long guideId;
    private String guideName;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Duration is required")
    private String duration;

    @NotNull(message = "Max group size is required")
    @Min(value = 1, message = "Max group size must be at least 1")
    private Integer maxGroupSize;

    private Integer availableSpots;

    @NotNull(message = "Price per person is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal pricePerPerson;

    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    private LocalDateTime createdAt;
    private TourStatus status;
    private String meetingPoint;
    private String imageUrl;
}
