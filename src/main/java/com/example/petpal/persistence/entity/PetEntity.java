package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pet")
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "breed_id")
    private BreedEntity breed;

    @NotNull
    @NotBlank
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @NotNull
    private double weight;

    @NotNull
    @NotBlank
    private String image;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArrayList<VaccinationRecordEntity> vaccinationRecords;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArrayList<HealthRecordEntity> healthRecords;
}
