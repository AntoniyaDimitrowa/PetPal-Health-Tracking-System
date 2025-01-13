package com.example.petpal.controller.dto.pet;

import com.example.petpal.business.domain.enums.Gender;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class UpdatePetDTO {
    @NotNull(message = "Pet ID is required.")
    private Long id;

    @NotBlank(message = "Name is required.")
    @Size(min = 1, message = "Name must not be empty.")
    private String name;

    @NotNull(message = "Breed is required.")
    private Long breedId;

    @NotNull(message = "Gender is required.")
    private Gender gender;

    @NotNull(message = "Birthdate is required.")
    private Date birthdate;

    @DecimalMin(value = "0.01", message = "Weight must be greater than 0.")
    private double weight;

    private String image;
}
