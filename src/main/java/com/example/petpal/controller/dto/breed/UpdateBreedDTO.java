package com.example.petpal.controller.dto.breed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UpdateBreedDTO {
    private Long id;
    private String name;
    private String description;
    private Long normalMoodId;
    private double minimumExercisePerDay;
    private List<String> commonHealthProblems;
}
