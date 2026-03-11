package com.spring.tour_reservation;

import com.spring.tour_reservation.model.Tour;
import com.spring.tour_reservation.model.User;
import com.spring.tour_reservation.model.UserRole;
import com.spring.tour_reservation.repository.TourRepository;
import com.spring.tour_reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TourRepository tourRepository;

    @Override
    public void run(String @NonNull ... args) {

        if (userRepository.count() == 0) {
            

            User guide = User.builder()
                    .email("guide@tourly.com")
                    .firstName("Ivan")
                    .lastName("Ivanov")
                    .password("secret123") 
                    .role(UserRole.GUIDE)
                    .bio("Veteran guide with 15 years of experience in the mountains.")
                    .build();
            userRepository.save(guide);

            User traveler = User.builder()
                    .email("user@tourly.com")
                    .firstName("Maria")
                    .lastName("Georgieva")
                    .password("pass123")
                    .role(UserRole.TRAVELER)
                    .build();
            userRepository.save(traveler);

            Tour rilaTour = Tour.builder()
                    .guide(guide)
                    .title("The Seven Rila Lakes")
                    .description("An incredible one-day adventure in the heart of the Rila Mountains.")
                    .location("Rila, Bulgaria")
                    .duration("08:30")
                    .maxGroupSize(15)
                    .availableSpots(15)
                    .pricePerPerson(45.0)
                    .scheduledDate(LocalDate.now().plusDays(5))
                    .startTime(LocalTime.of(8, 0))
                    .imageUrl("https://freesofiatour.com/wp-content/uploads/2018/05/seven-rila-lakes-how-to-get-to-1200x675.jpeg") 
                    .build();

            Tour varnaTour = Tour.builder()
                    .guide(guide)
                    .title("Varna & Aladzha Monastery Tour")
                    .description("Visit the seaside and an ancient cave monastery.")
                    .location("Varna, Bulgaria")
                    .duration("12:00")
                    .maxGroupSize(8)
                    .availableSpots(8)
                    .pricePerPerson(120.0)
                    .scheduledDate(LocalDate.now().plusWeeks(2))
                    .startTime(LocalTime.of(9, 30))
                    .imageUrl("https://seadream.com/images/ports/Varna,%20Bulgaria.jpeg") 
                    .build();

            tourRepository.save(rilaTour);
            tourRepository.save(varnaTour);

            System.out.println("DB is initialized");
        }
    }
}
