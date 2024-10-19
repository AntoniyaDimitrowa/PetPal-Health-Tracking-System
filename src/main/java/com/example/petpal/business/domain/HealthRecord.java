package com.example.petpal.business.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class HealthRecord {
    @Setter(AccessLevel.NONE)
    private Long id;
    private Date date;
    private double foodIntake;  // in grams
    private double waterIntake; // in liters
    private Mood mood;
    private int activityLevel;    // Activity level (1-10 scale)
    private String socialInteraction;
    private String notes;
}
