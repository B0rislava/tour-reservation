package com.spring.tour_reservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tour_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "tourDetails")
    private Tour tour;

    @Column(name = "description", length = 3000)
    private String description;

    @Column(name = "whats_included", length = 1000)
    private String whatsIncluded;

    @Column(name = "meeting_point", length = 500)
    private String meetingPoint;

    @Column(name = "requirements", length = 1000)
    private String requirements;
}
