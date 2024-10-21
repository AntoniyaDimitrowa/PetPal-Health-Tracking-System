package com.example.petpal.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "health_record")
public class HealthRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    @NotNull
    private double foodIntake;  // in grams

    @NotNull
    private double waterIntake; // in liters

    @NotNull
    @ManyToOne
    @JoinColumn(name = "mood_id")
    private MoodEntity mood;

    //Behavior
    @NotNull
    private int activityLevel;    // Activity level (1-10 scale)

    @NotNull
    @NotBlank
    private String socialInteraction;

    private String notes;
}
