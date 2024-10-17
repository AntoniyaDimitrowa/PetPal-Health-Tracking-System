package com.example.petpal.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class HealthRecordEntity {
    private long id;
    private Date date;
    private double foodIntake;  // in grams
    private double waterIntake; // in liters
    private String mood;        // "Happy", "Lethargic"
    private String behavior;    // "Active", "Restless"
    private String notes;
}
