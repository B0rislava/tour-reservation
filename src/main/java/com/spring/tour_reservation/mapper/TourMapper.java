package com.spring.tour_reservation.mapper;

import com.spring.tour_reservation.dto.TourDto;
import com.spring.tour_reservation.model.Tag;
import com.spring.tour_reservation.model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class TourMapper {

    @Mapping(target = "guideId", source = "guide.id")
    @Mapping(target = "guideName", expression = "java(tour.getGuide().getFirstName() + \" \" + tour.getGuide().getLastName())")
    @Mapping(target = "rating", constant = "0.0")
    @Mapping(target = "reviewsCount", constant = "0")
    @Mapping(target = "imageUrl", expression = "java(getMainImageUrl(tour))")
    public abstract TourDto toDto(Tour tour);

    protected String getMainImageUrl(Tour tour) {
        if (tour.getTourImages() == null || tour.getTourImages().isEmpty()) {
            return null;
        }
        return tour.getTourImages().iterator().next().getContent();
    }

    protected Set<String> mapTags(Set<Tag> tags) {
        if (tags == null) return null;
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }
}
