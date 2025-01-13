package com.example.petpal.controller.dto.health;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateBreedHealthInfoDTO {
    @NotNull(message = "Breed is required.")
    private Long breedId;
    @NotNull(message = "User is required.")
    private Long userId;
    @NotNull(message = "Age Range Start is required.")
    @Min(value = 1, message = "Age Range End must be greater than 0.")
    private int ageRangeStart;
    @NotNull(message = "Age Range End is required.")
    @Min(value = 1, message = "Age Range End must be greater than 0.")
    private int ageRangeEnd;
    @NotNull(message = "Normal Food Intake is required.")
    @DecimalMin(value = "0.01", message = "Normal Food Intake must be greater than 0.")
    private double normalFoodIntake; // in grams
    @NotNull(message = "Normal Water Intake is required.")
    @DecimalMin(value = "0.01", message = "Normal Water Intake must be greater than 0.")
    private double normalWaterIntake; // in grams
    @NotNull(message = "Min Weight is required.")
    @DecimalMin(value = "0.01", message = "Min Weight must be greater than 0.")
    private double weightRangeMin; //in kilos
    @NotNull(message = "Max Weight is required.")
    @DecimalMin(value = "0.01", message = "Max Weight must be greater than 0.")
    private double weightRangeMax; //in kilos

    @AssertTrue(message = "Age Range Start must be less than Age Range End.")
    private boolean isAgeRangeValid() {
        return ageRangeStart < ageRangeEnd;
    }

    @AssertTrue(message = "Weight Range Min must be less than Weight Range Max.")
    private boolean isWeightRangeValid() {
        return weightRangeMin < weightRangeMax;
    }
}
