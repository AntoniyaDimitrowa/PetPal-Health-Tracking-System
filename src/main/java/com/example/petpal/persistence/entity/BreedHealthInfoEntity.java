package com.example.petpal.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "breed_health_info")
public class BreedHealthInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "breed_id")
    private BreedEntity breed;

    @NotNull
    private int ageRangeStart;

    @NotNull
    private int ageRangeEnd;

    @NotNull
    private double normalFoodIntake;  // in grams

    @NotNull
    private double normalWaterIntake; // in liters
}
