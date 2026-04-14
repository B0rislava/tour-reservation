package com.spring.tour_reservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequestDto {

    @NotNull(message = "Tour ID is required")
    private Long tourId;

    @Min(value = 1, message = "Participants must be at least 1")
    private int participants;
}
