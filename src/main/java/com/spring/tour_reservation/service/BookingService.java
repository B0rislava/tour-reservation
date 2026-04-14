package com.spring.tour_reservation.service;

import com.spring.tour_reservation.dto.BookingDto;
import com.spring.tour_reservation.exception.TourReservationException;
import com.spring.tour_reservation.model.Booking;
import com.spring.tour_reservation.model.BookingStatus;
import com.spring.tour_reservation.model.Tour;
import com.spring.tour_reservation.model.User;
import com.spring.tour_reservation.repository.BookingRepository;
import com.spring.tour_reservation.repository.TourRepository;
import com.spring.tour_reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.spring.tour_reservation.mapper.BookingMapper;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public void bookTour(Long userId, Long tourId, int participants) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TourReservationException("User not found"));

        if (user.getRole() == com.spring.tour_reservation.model.UserRole.GUIDE || 
            user.getRole() == com.spring.tour_reservation.model.UserRole.ADMIN) {
            throw new TourReservationException("Only travelers can book tours!");
        }

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new TourReservationException("Tour not found"));

        if (bookingRepository.existsByUserIdAndTourId(userId, tourId)) {
            throw new TourReservationException("You have already booked this tour!");
        }

        if (tour.getAvailableSpots() < participants) {
            throw new TourReservationException("Not enough available spots!");
        }

        tour.setAvailableSpots(tour.getAvailableSpots() - participants);
        tourRepository.save(tour);

        Booking booking = Booking.builder()
                .user(user)
                .tour(tour)
                .numberOfParticipants(participants)
                .bookingDate(LocalDateTime.now())
                .status(BookingStatus.CONFIRMED)
                .build();

        bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new TourReservationException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new TourReservationException("You are not authorized to cancel this booking!");
        }

        Tour tour = booking.getTour();
        tour.setAvailableSpots(tour.getAvailableSpots() + booking.getNumberOfParticipants());
        tourRepository.save(tour);

        bookingRepository.delete(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsForUser(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new TourReservationException("User not found: " + email));
    }
}
