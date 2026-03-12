package com.spring.tour_reservation.service;

import com.spring.tour_reservation.dto.BookingDto;
import com.spring.tour_reservation.model.Booking;
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

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;
    private final UserRepository userRepository;

    @Transactional
    public void bookTour(Long userId, Long tourId, int participants) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        if (bookingRepository.existsByUserIdAndTourId(userId, tourId)) {
            throw new RuntimeException("You have already booked this tour!");
        }

        if (tour.getAvailableSpots() < participants) {
            throw new RuntimeException("Not enough available spots!");
        }

        tour.setAvailableSpots(tour.getAvailableSpots() - participants);
        tourRepository.save(tour);

        Booking booking = Booking.builder()
                .user(user)
                .tour(tour)
                .numberOfParticipants(participants)
                .bookingDate(LocalDateTime.now())
                .status("CONFIRMED")
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        mapToDto(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsForUser(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private BookingDto mapToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .userName(booking.getUser().getFirstName() + " " + booking.getUser().getLastName())
                .tourId(booking.getTour().getId())
                .tourTitle(booking.getTour().getTitle())
                .numberOfParticipants(booking.getNumberOfParticipants())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .hasReview(booking.getReview() != null)
                .build();
    }
}
