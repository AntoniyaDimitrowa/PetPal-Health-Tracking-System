package com.example.petpal.controller.dto.health;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class CreateHealthRecordDTO {
    @NotNull(message = "Date is required.")
    private Date date;
    @NotNull(message = "Food Intake is required.")
    @DecimalMin(value = "0.01", message = "Food Intake must be greater than 0.")
    private double foodIntake;  // in grams
    @NotNull(message = "Water Intake is required.")
    @DecimalMin(value = "0.01", message = "Water Intake must be greater than 0.")
    private double waterIntake; // in liters
    @NotNull(message = "Mood is required.")
    private Long moodId;
    @NotNull(message = "Activity level is required.")
    @Min(value = 1, message = "Activity level must be greater than 0.")
    @Max(value = 10, message = "Activity level must be less or equal to 10.")
    private int activityLevel;    // Activity level (1-10 scale)
    @NotNull(message = "Social Interaction is required.")
    private String socialInteraction;
    private String notes;
}
