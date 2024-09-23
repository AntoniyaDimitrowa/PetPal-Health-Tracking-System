package com.example.petpal.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class HealthRecord {
    private long id;
    private Date date;
    private double foodIntake;  // in grams
    private double waterIntake; // in liters
    private String mood;        // "Happy", "Lethargic"
    private String behavior;    // "Active", "Restless"
    private String notes;
}
