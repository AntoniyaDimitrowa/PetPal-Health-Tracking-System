package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.Mood;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BreedDTO {
    private String name;
    private String description;
    private MoodDTO normalMood;
    private double minimumExercisePerDay; //in hours
    private ArrayList<String> commonHealthProblems;
}
