package com.spring.tour_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private String role;
}
