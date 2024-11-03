package com.example.petpal.business.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Breed {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private String description;
    private Mood normalMood;
    private double minimumExercisePerDay; //in hours
    private List<String> commonHealthProblems;

}
