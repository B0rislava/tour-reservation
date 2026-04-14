package com.spring.tour_reservation.service;

import com.spring.tour_reservation.dto.RegistrationDto;
import com.spring.tour_reservation.dto.UserDto;
import com.spring.tour_reservation.dto.UserUpdateRequestDto;
import com.spring.tour_reservation.exception.TourReservationException;
import com.spring.tour_reservation.model.User;
import com.spring.tour_reservation.model.UserRole;
import com.spring.tour_reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.tour_reservation.mapper.UserMapper;
import com.spring.tour_reservation.mapper.TourMapper;
import com.spring.tour_reservation.repository.TourRepository;
import com.spring.tour_reservation.model.Tour;
import com.spring.tour_reservation.dto.TourDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TourMapper tourMapper;

    @Transactional
    public void registerUser(RegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new TourReservationException("User with this email already exists!");
        }

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new TourReservationException("Passwords do not match!");
        }

        UserRole assignedRole = UserRole.TRAVELER;
        if (registrationDto.getRole() != null && registrationDto.getRole().equalsIgnoreCase("GUIDE")) {
            assignedRole = UserRole.GUIDE;
        }

        User user = User.builder()
                .email(registrationDto.getEmail())
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .role(assignedRole)
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new TourReservationException("User not found"));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateUserProfile(String email, UserUpdateRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new TourReservationException("User not found"));
        
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            user.setLastName(request.getLastName().trim());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio().trim());
        }
        
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new TourReservationException("User not found"));
        
        if (user.getRole() == UserRole.ADMIN) {
            throw new TourReservationException("Administrators cannot delete their own accounts!");
        }
        
        userRepository.delete(user);
    }

    @Transactional
    public void addSavedTour(String email, Long tourId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new TourReservationException("User not found"));
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new TourReservationException("Tour not found"));

        if (user.getSavedTours().contains(tour)) {
            throw new TourReservationException("Tour is already saved");
        }

        user.getSavedTours().add(tour);
        userRepository.save(user);
    }

    @Transactional
    public void removeSavedTour(String email, Long tourId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new TourReservationException("User not found"));
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new TourReservationException("Tour not found"));

        if (!user.getSavedTours().contains(tour)) {
            throw new TourReservationException("Tour is not in saved list");
        }

        user.getSavedTours().remove(tour);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<TourDto> getSavedTours(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new TourReservationException("User not found"));
        
        return user.getSavedTours().stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserById(Long id, String currentUserEmail) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new TourReservationException("User not found"));
        
        if (userToDelete.getEmail().equals(currentUserEmail)) {
            throw new TourReservationException("You cannot delete your own account!");
        }
        
        userRepository.deleteById(id);
    }
}
