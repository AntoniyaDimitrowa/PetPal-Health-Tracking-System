package com.example.petpal.business.domain;

import java.util.ArrayList;
import java.util.Date;

public class HealthRecord {
    private long id;
    private Date date;
    private double foodIntake;  // in grams
    private double waterIntake; // in liters
    private String mood;        // "Happy", "Lethargic"
    private String behavior;    // "Active", "Restless"
    private String notes;
}
