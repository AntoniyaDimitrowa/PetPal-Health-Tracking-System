package com.example.petpal.controller.dto.breed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
public class UpdateBreedDTO {
    private long id;
    private String name;
    private String description;
    private long normalMoodId;
    private double minimumExercisePerDay;
    private ArrayList<String> commonHealthProblems;
}
