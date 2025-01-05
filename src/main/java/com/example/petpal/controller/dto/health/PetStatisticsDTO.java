package com.example.petpal.controller.dto.health;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PetStatisticsDTO {

    private List<FoodWaterIntakeDTO> foodIntake;
    private List<FoodWaterIntakeDTO> waterIntake;
    private List<ActivityLevelDTO> activityLevel;
    private List<MoodDistributionDTO> moodDistribution;

    @Data
    @AllArgsConstructor
    public static class FoodWaterIntakeDTO {
        private Date date;
        private Double intake;
        private Double norm;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityLevelDTO {
        private Date date;
        private Long level;
        private Double norm;
    }

    @Data
    @AllArgsConstructor
    public static class MoodDistributionDTO {
        private String mood;
        private Long value;

    }
}
