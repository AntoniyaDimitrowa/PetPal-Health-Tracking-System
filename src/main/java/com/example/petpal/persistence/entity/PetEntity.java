package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.Image;
import com.example.petpal.business.domain.enums.Gender;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetEntity {
    private long id;
    private String name;
    private BreedEntity breed;
    private Gender gender;
    private Date birthdate;
    private double weight;
    private Image image;

    private ArrayList<VaccinationRecordEntity> vaccinationRecords;
    private ArrayList<HealthRecordEntity> healthRecords;
}
