package com.spring.tour_reservation.service;

import com.spring.tour_reservation.dto.TourDto;
import com.spring.tour_reservation.model.Tour;
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
        return tourRepository.findAll()
                .stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TourDto getTourById(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found!"));
        return tourMapper.toDto(tour);
    }

    @Transactional(readOnly = true)
    public List<TourDto> getToursByGuideEmail(String email) {
        User guide = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Guide not found"));

        return tourRepository.findByGuideId(guide.getId())
                .stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createTour(TourDto createDto, String guideEmail) {
        User guide = userRepository.findByEmail(guideEmail)
                .orElseThrow(() -> new RuntimeException("Guide not found"));

        if (guide.getRole() != UserRole.GUIDE) {
            throw new RuntimeException("Only guides can create tours!");
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
                .status("ACTIVE")
                .guide(guide)
                .build();

        tourRepository.save(tour);
        return tour.getId();
    }
}
