package com.example.petpal.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "breed")
public class BreedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 10000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "normal_mood_id", nullable = false)
    private MoodEntity normalMood;

    @Column(nullable = false)
    private double minimumExercisePerDay; // in hours

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "breed_health_problems", joinColumns = @JoinColumn(name = "breed_id"))
    @Column(name = "health_problem")
    private ArrayList<String> commonHealthProblems;

}
