package com.example.petpal.controller.dto.breed;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CreateBreedDTO {
    @NotNull(message = "Name is required.")
    @Size(min = 1, message = "Name must not be empty.")
    private String name;
    private String description;
    @NotNull(message = "Mood is required.")
    private Long normalMoodId;
    @NotNull(message = "Minimum Exercise Per Day is required.")
    @DecimalMin(value = "0.01", message = "Minimum Exercise Per Day must be greater than 0.")
    private double minimumExercisePerDay; //in hours
    private List<String> commonHealthProblems;
}
