package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.enums.Gender;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class PetEntity {
    private Long id;
    private String name;
    private BreedEntity breed;
    private Gender gender;
    private Date birthdate;
    private double weight;
    private String image;

    private ArrayList<VaccinationRecordEntity> vaccinationRecords;
    private ArrayList<HealthRecordEntity> healthRecords;
}
