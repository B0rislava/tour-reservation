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

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;


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
}
