package com.example.petpal.business.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class HealthRecord {
    @Setter(AccessLevel.NONE)
    private long id;
    private Date date;
    private double foodIntake;  // in grams
    private double waterIntake; // in liters
    private String mood;        // "Happy", "Lethargic"
    private String behavior;    // "Active", "Restless"
    private String notes;
}
