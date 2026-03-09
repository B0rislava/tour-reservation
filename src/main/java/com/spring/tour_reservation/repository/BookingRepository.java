package com.spring.tour_reservation.repository;

import com.spring.tour_reservation.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByTourId(Long tourId);

    boolean existsByUserIdAndTourId(Long userId, Long tourId);
}
