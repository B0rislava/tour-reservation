package com.spring.tour_reservation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:.env", ignoreResourceNotFound = true)
@OpenAPIDefinition(info = @Info(title = "Tour Reservation API", version = "1.0", description = "API Documentation for the Tour Reservation Application"))
public class TourReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourReservationApplication.class, args);
	}

}
