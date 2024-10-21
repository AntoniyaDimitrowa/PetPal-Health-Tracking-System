package com.example.petpal.controller.dto.health;

import com.example.petpal.business.domain.Breed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BreedHealthInfoDTO {
    private Long id;
    private Long breedId;
    private int ageRangeStart;
    private int ageRangeEnd;
    private double normalFoodIntake; // in grams
    private double normalWaterIntake; // in grams
}
