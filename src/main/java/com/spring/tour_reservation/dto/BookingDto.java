package com.spring.tour_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    
    private Long userId;
    private String userName; 
    
    private Long tourId;
    private String tourTitle;
    
    private Integer numberOfParticipants;
    private LocalDateTime bookingDate;
    private String status;
    
    private boolean hasReview; 
}
