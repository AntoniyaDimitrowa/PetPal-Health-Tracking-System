package com.example.petpal.controller.dto.health;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class CreateHealthRecordDTO {
    private Long id;
    private Date date;
    private double foodIntake;  // in grams
    private double waterIntake; // in liters
    private Long moodId;
    private int activityLevel;    // Activity level (1-10 scale)
    private String socialInteraction;
    private String notes;
}
