package com.spring.tour_reservation.mapper;

import com.spring.tour_reservation.dto.UserDto;
import com.spring.tour_reservation.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
}
