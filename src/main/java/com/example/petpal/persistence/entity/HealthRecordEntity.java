package com.example.petpal.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @NotNull
    @Column(nullable = false)
    private double foodIntake;  // in grams

    @NotNull
    @Column(nullable = false)
    private double waterIntake; // in liters

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mood_id", nullable = false)
    private MoodEntity mood;

    //Behavior
    @NotNull
    @Min(1)
    @Max(10)
    @Column(nullable = false)
    private int activityLevel;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String socialInteraction;

    @Column(length = 1000)
    private String notes;
}
