package com.zerobase.accommodation;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AccommodationApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccommodationApplication.class,args);
	}
}
