package com.spring.tour_reservation;

import com.spring.tour_reservation.model.Tour;
import com.spring.tour_reservation.model.TourImage;
import com.spring.tour_reservation.model.User;
import com.spring.tour_reservation.model.UserRole;
import com.spring.tour_reservation.repository.TourRepository;
import com.spring.tour_reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.admin-password}")
    private String adminPassword;

    @Value("${app.security.guide-password}")
    private String guidePassword;

    @Override
    public void run(String @NonNull ... args) {

        userRepository.findByEmail("admin@gmail.com").ifPresentOrElse(
                user -> {
                    if (user.getRole() != UserRole.ADMIN) {
                        user.setRole(UserRole.ADMIN);
                        userRepository.save(user);
                        log.info("Updated existing user to ADMIN role: admin@gmail.com");
                    }
                },
                () -> {
                    User admin = User.builder()
                            .email("admin@gmail.com")
                            .firstName("Admin")
                            .lastName("User")
                            .password(passwordEncoder.encode(adminPassword))
                            .role(UserRole.ADMIN)
                            .bio("System administrator.")
                            .build();
                    userRepository.save(admin);
                    log.info("Created new admin user: admin@gmail.com");
                }
        );

        User guide = userRepository.findByEmail("guide@gmail.com").orElseGet(() -> {
            User newGuide = User.builder()
                    .email("guide@gmail.com")
                    .firstName("Ivan")
                    .lastName("Ivanov")
                    .password(passwordEncoder.encode(guidePassword))
                    .role(UserRole.GUIDE)
                    .bio("Veteran guide with 15 years of experience in the mountains.")
                    .build();
            return userRepository.save(newGuide);
        });

        if (guide.getRole() != UserRole.GUIDE) {
            guide.setRole(UserRole.GUIDE);
            userRepository.save(guide);
        }

        if (tourRepository.count() == 0) {
            Tour rilaTour = Tour.builder()
                    .guide(guide)
                    .title("The Seven Rila Lakes")
                    .description("An incredible one-day adventure in the heart of the Rila Mountains.")
                    .location("Rila, Bulgaria")
                    .duration("05:30")
                    .maxGroupSize(15)
                    .availableSpots(15)
                    .pricePerPerson(BigDecimal.valueOf(45.0))
                    .meetingPoint("Alexander Nevsky Cathedral, Sofia")
                    .scheduledDate(LocalDate.now().plusDays(5))
                    .startTime(LocalTime.of(8, 0))
                    .build();

            rilaTour.setTourImage(TourImage.builder()
                    .tour(rilaTour)
                    .fileName("rila-lakes.jpg")
                    .fileType("image/jpeg")
                    .content("https://freesofiatour.com/wp-content/uploads/2018/05/seven-rila-lakes-how-to-get-to-1200x675.jpeg")
                    .build());

            Tour varnaTour = Tour.builder()
                    .guide(guide)
                    .title("Varna & Aladzha Monastery Tour")
                    .description("Visit the seaside and an ancient cave monastery.")
                    .location("Varna, Bulgaria")
                    .duration("2:00")
                    .maxGroupSize(8)
                    .availableSpots(8)
                    .pricePerPerson(BigDecimal.valueOf(120.0))
                    .meetingPoint("Varna Central Bus Station")
                    .scheduledDate(LocalDate.now().plusWeeks(2))
                    .startTime(LocalTime.of(9, 30))
                    .build();

            varnaTour.setTourImage(TourImage.builder()
                    .tour(varnaTour)
                    .fileName("varna.jpg")
                    .fileType("image/jpeg")
                    .content("https://seadream.com/images/ports/Varna,%20Bulgaria.jpeg")
                    .build());

            tourRepository.save(rilaTour);
            tourRepository.save(varnaTour);
            log.info("Initialized demo tours.");
        }
    }
}
