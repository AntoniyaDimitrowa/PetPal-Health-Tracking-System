package com.example.petpal.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BreedHealthInfo {
    private Breed breed;
    private int ageRangeStart;
    private int ageRangeEnd;
    private double normalFoodIntake; // in grams
    private double normalWaterIntake; // in grams
}
