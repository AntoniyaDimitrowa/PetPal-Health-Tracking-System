package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.Owner;
import com.example.petpal.business.domain.VaccinationRecord;
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
    private ArrayList<VaccinationRecordEntity> vaccinationRecords;
    private ArrayList<HealthRecordEntity> healthRecords;
}
