package com.example.petpal.controller.dto.health;

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
    private String mood;        // "Happy", "Lethargic"
    private String behavior;    // "Active", "Restless"
    private String notes;
}
