package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.Mood;
import lombok.*;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
public class BreedEntity {
    private long id;
    private String name;
    private String description;
    private MoodEntity normalMood;
    private double minimumExercisePerDay; //in hours
    private ArrayList<String> commonHealthProblems;
}
