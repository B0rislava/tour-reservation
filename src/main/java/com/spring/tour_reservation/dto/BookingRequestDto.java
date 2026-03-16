package com.spring.tour_reservation.dto;

import lombok.Data;

@Data
public class BookingRequestDto {
    private Long tourId;
    private int participants;
}
