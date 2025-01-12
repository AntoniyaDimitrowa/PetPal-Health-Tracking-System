package com.example.petpal.controller.dto.health;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateBreedHealthInfoDTO {
    private Long breedId;
    private Long userId;
    private int ageRangeStart;
    private int ageRangeEnd;
    private double normalFoodIntake; // in grams
    private double normalWaterIntake; // in grams
    private double weightRangeMin; //in kilos
    private double weightRangeMax; //in kilos
}
