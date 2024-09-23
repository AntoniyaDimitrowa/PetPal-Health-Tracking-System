package com.example.petpal.business.domain;

import lombok.*;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Breed {
    private long id;
    private String name;
    private String description;
    private Mood normalMood;
    private double minimumExercisePerDay; //in hours
    private ArrayList<String> commonHealthProblems;
}
