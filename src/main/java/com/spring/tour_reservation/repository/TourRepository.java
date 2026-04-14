package com.spring.tour_reservation.repository;

import com.spring.tour_reservation.model.Tour;
import com.spring.tour_reservation.model.TourStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    List<Tour> findByGuideId(Long guideId);

    List<Tour> findByStatus(TourStatus status);

    List<Tour> findByLocationContainingIgnoreCase(String location);
}
