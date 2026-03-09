package com.spring.tour_reservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tours")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ManyToOne - many TOURS -> one USER
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    private User guide;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "max_group_size", nullable = false)
    private Integer maxGroupSize;

    @Column(name = "available_spots", nullable = false)
    private Integer availableSpots;

    @Column(name = "price_per_person", nullable = false)
    private Double pricePerPerson;

    @Column(name = "whats_included", length = 1000)
    private String whatsIncluded;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "status", nullable = false)
    @Builder.Default
    private String status = "ACTIVE";

    @Column(name = "rating")
    @Builder.Default
    private Double rating = 0.0;

    @Column(name = "reviews_count")
    @Builder.Default
    private Integer reviewsCount = 0;

    @Column(name = "meeting_point")
    private String meetingPoint;

    @Column(name = "image_url")
    private String imageUrl;

    // ManyToMany - many TOURS -> many TAGS
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tour_tags",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    // OneToMany - one TOUR -> many BOOKINGS
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Booking> bookings = new HashSet<>();
}
