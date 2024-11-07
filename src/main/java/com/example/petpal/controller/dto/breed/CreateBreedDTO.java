package com.example.petpal.controller.dto.breed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CreateBreedDTO {
    private String name;
    private String description;
    private Long normalMoodId;
    private double minimumExercisePerDay; //in hours
    private List<String> commonHealthProblems;
}
