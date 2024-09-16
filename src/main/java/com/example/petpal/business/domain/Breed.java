package com.example.petpal.business.domain;

import java.util.ArrayList;

public class Breed {
    private long id;
    private String name;
    private String description;
    private Mood normalMood;
    private double minimumExercisePerDay; //in hours
    private ArrayList<String> commonHealthProblems;
}
