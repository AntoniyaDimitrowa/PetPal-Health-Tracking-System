package com.example.petpal.controller.dto.pet;


import java.util.Date;
import java.util.List;
import com.example.petpal.business.domain.enums.Gender;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class CreatePetDTO {

    @NotNull(message = "Name is required.")
    @Size(min = 1, message = "Name must not be empty.")
    private String name;

    @NotNull(message = "Breed ID is required.")
    private Long breedId;

    @NotNull(message = "User ID is required.")
    private Long userId;

    @NotNull(message = "Gender is required.")
    private Gender gender;

    @NotNull(message = "Birthdate is required.")
    private Date birthdate;

    @DecimalMin(value = "0.01", message = "Weight must be greater than 0.")
    private double weight;

    private String image;

    private List<Long> vaccinationRecordsIds;
}
