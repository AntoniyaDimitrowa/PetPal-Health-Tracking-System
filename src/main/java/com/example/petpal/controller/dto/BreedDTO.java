package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.Mood;
import lombok.*;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BreedDTO {

    private String name;
    private String description;
    private MoodDTO normalMood;
    private double minimumExercisePerDay; //in hours
    private ArrayList<String> commonHealthProblems;
}
