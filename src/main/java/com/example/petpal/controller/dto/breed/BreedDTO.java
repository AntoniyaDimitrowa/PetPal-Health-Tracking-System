package com.example.petpal.controller.dto.breed;

import com.example.petpal.controller.dto.mood.MoodDTO;
import lombok.*;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
public class BreedDTO {
    private long id;
    private String name;
    private String description;
    private MoodDTO normalMood;;
    private double minimumExercisePerDay; //in hours
    private ArrayList<String> commonHealthProblems;
}
