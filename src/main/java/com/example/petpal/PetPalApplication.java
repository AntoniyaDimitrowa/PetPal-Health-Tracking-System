package com.example.petpal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.petpal.persistence.entity")
public class PetPalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetPalApplication.class, args);
	}

}
