package com.spring.tour_reservation.mapper;

import com.spring.tour_reservation.dto.TourDto;
import com.spring.tour_reservation.model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class TourMapper {

    @Mapping(target = "guideId", source = "guide.id")
    @Mapping(target = "guideName", expression = "java(tour.getGuide().getFirstName() + \" \" + tour.getGuide().getLastName())")
    @Mapping(target = "imageUrl", expression = "java(getMainImageUrl(tour))")
    public abstract TourDto toDto(Tour tour);

    protected String getMainImageUrl(Tour tour) {
        if (tour.getTourImage() == null) {
            return null;
        }
        return tour.getTourImage().getContent();
    }
}
