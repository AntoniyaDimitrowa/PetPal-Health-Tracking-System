package com.example.petpal.persistence.entity;

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
public class BreedEntity {
    private long id;
    private String name;
    private String description;
    private MoodEntity normalMood;
    private double minimumExercisePerDay; //in hours
    private ArrayList<String> commonHealthProblems;
}
