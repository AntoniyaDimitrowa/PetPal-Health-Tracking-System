package com.example.petpal.controller.dto.breed;

import com.example.petpal.controller.dto.mood.MoodDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BreedDTO {
    private Long id;
    private String name;
    private String description;
    private MoodDTO normalMood;
    private double minimumExercisePerDay; //in hours
    private List<String> commonHealthProblems;
}
