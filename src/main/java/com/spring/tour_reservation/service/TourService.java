package com.spring.tour_reservation.service;

import com.spring.tour_reservation.dto.TourDto;
import com.spring.tour_reservation.model.Tag;
import com.spring.tour_reservation.model.Tour;
import com.spring.tour_reservation.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;

    @Transactional(readOnly = true)
    public List<TourDto> getAllTours() {
        return tourRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TourDto mapToDto(Tour tour) {
        return TourDto.builder()
                .id(tour.getId())
                .guideId(tour.getGuide().getId())
                .guideName(tour.getGuide().getFirstName() + " " + tour.getGuide().getLastName())
                .title(tour.getTitle())
                .description(tour.getDescription())
                .location(tour.getLocation())
                .duration(tour.getDuration())
                .maxGroupSize(tour.getMaxGroupSize())
                .availableSpots(tour.getAvailableSpots())
                .pricePerPerson(tour.getPricePerPerson())
                .imageUrl(tour.getImageUrl())
                .tags(tour.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .build();
    }
}
