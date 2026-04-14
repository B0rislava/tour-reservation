package com.spring.tour_reservation.service;

import com.spring.tour_reservation.dto.TourDto;
import com.spring.tour_reservation.exception.TourReservationException;
import com.spring.tour_reservation.model.Tour;
import com.spring.tour_reservation.model.TourImage;
import com.spring.tour_reservation.model.TourStatus;
import com.spring.tour_reservation.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.spring.tour_reservation.mapper.TourMapper;

import com.spring.tour_reservation.model.User;
import com.spring.tour_reservation.model.UserRole;
import com.spring.tour_reservation.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<TourDto> getAllTours() {
        return tourRepository.findByStatus(TourStatus.ACTIVE)
                .stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TourDto> searchToursByLocation(String location) {
        return tourRepository.findByLocationContainingIgnoreCase(location)
                .stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TourDto getTourById(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new TourReservationException("Tour not found!"));
        return tourMapper.toDto(tour);
    }

    @Transactional(readOnly = true)
    public List<TourDto> getToursByGuideEmail(String email) {
        User guide = userRepository.findByEmail(email)
                .orElseThrow(() -> new TourReservationException("Guide not found"));

        return tourRepository.findByGuideId(guide.getId())
                .stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createTour(TourDto createDto, String guideEmail) {
        User guide = userRepository.findByEmail(guideEmail)
                .orElseThrow(() -> new TourReservationException("Guide not found"));

        if (guide.getRole() != UserRole.GUIDE) {
            throw new TourReservationException("Only guides can create tours!");
        }

        Tour tour = Tour.builder()
                .title(createDto.getTitle())
                .description(createDto.getDescription())
                .location(createDto.getLocation())
                .duration(createDto.getDuration())
                .maxGroupSize(createDto.getMaxGroupSize())
                .availableSpots(createDto.getMaxGroupSize())
                .pricePerPerson(createDto.getPricePerPerson())
                .meetingPoint(createDto.getMeetingPoint())
                .scheduledDate(createDto.getScheduledDate())
                .startTime(createDto.getStartTime())
                .status(TourStatus.ACTIVE)
                .guide(guide)
                .build();

        if (createDto.getImageUrl() != null && !createDto.getImageUrl().isEmpty()) {
            TourImage tourImage = TourImage.builder()
                    .tour(tour)
                    .content(createDto.getImageUrl())
                    .fileName("api-upload.jpg")
                    .fileType("image/jpeg")
                    .build();
            tour.setTourImage(tourImage);
        }

        tourRepository.save(tour);
        return tour.getId();
    }

    @Transactional
    public void deleteTour(Long id, String guideEmail) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new TourReservationException("Tour not found!"));
        
        // Only the guide who created it OR an admin can delete it
        if (!tour.getGuide().getEmail().equals(guideEmail)) {
            User user = userRepository.findByEmail(guideEmail)
                    .orElseThrow(() -> new TourReservationException("User not found"));
            if (user.getRole() != UserRole.ADMIN) {
                throw new TourReservationException("You are not authorized to delete this tour!");
            }
        }
        
        tourRepository.delete(tour);
    }

    @Transactional
    public void updateTour(Long id, TourDto updateDto, String guideEmail) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new TourReservationException("Tour not found!"));
        
        if (!tour.getGuide().getEmail().equals(guideEmail)) {
            throw new TourReservationException("You are not authorized to edit this tour!");
        }

        tour.setTitle(updateDto.getTitle());
        tour.setDescription(updateDto.getDescription());
        tour.setLocation(updateDto.getLocation());
        tour.setDuration(updateDto.getDuration());
        tour.setPricePerPerson(updateDto.getPricePerPerson());
        tour.setMaxGroupSize(updateDto.getMaxGroupSize());
        tour.setMeetingPoint(updateDto.getMeetingPoint());
        tour.setScheduledDate(updateDto.getScheduledDate());
        tour.setStartTime(updateDto.getStartTime());
        
        if (updateDto.getImageUrl() != null && !updateDto.getImageUrl().isEmpty()) {
            TourImage newImage = TourImage.builder()
                    .tour(tour)
                    .content(updateDto.getImageUrl())
                    .fileName("api-upload.jpg")
                    .fileType("image/jpeg")
                    .build();
            tour.setTourImage(newImage);
        }
        
        tourRepository.save(tour);
    }
}
