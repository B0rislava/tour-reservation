package com.spring.tour_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourDto {
    private Long id;
    private Long guideId;
    private String guideName; 
    private String title;
    private String description;
    private String location;
    private String duration;
    private Integer maxGroupSize;
    private Integer availableSpots;
    private Double pricePerPerson;
    private String whatsIncluded;
    private LocalDate scheduledDate;
    private LocalTime startTime;
    private LocalDateTime createdAt;
    private String status;
    private Double rating;
    private Integer reviewsCount;
    private String meetingPoint;
    private String imageUrl;
    
    private Set<String> tags;
}
