package com.example.petpal.business.domain;

import com.example.petpal.business.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Pet {
    private long id;
    private String name;
    private Breed breed;
    private Gender gender;
    private Date birthdate;
    private double weight;
    private ArrayList<VaccinationRecord> vaccinationRecords;
    private ArrayList<HealthRecord> healthRecords;
}
