package com.spring.tour_reservation.mapper;

import com.spring.tour_reservation.dto.BookingDto;
import com.spring.tour_reservation.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", expression = "java(booking.getUser().getFirstName() + \" \" + booking.getUser().getLastName())")
    @Mapping(target = "tourId", source = "tour.id")
    @Mapping(target = "tourTitle", source = "tour.title")
    @Mapping(target = "totalPrice", expression = "java(booking.getTour().getPricePerPerson().multiply(java.math.BigDecimal.valueOf(booking.getNumberOfParticipants())))")
    BookingDto toDto(Booking booking);

}
