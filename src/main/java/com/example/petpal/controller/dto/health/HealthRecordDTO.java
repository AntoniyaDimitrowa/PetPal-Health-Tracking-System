package com.example.petpal.controller.dto.health;

import com.example.petpal.controller.dto.mood.MoodDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class HealthRecordDTO {
    private Date date;
    private double foodIntake;  // in grams
    private double waterIntake; // in liters
    private MoodDTO mood;
    private int activityLevel;    // Activity level (1-10 scale)
    private String socialInteraction;
    private String notes;
}
