package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.Breed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BreedHealthInfoDTO {

    private BreedDTO breed;
    private int ageRangeStart;
    private int ageRangeEnd;
    private double normalFoodIntake; // in grams
    private double normalWaterIntake; // in grams
}
