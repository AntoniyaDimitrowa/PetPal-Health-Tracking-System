package com.example.petpal.business.domain;

import com.example.petpal.business.domain.enums.Gender;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Pet {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private Breed breed;
    private Gender gender;
    private Date birthdate;
    private double weight;
    private String image;
    private ArrayList<VaccinationRecord> vaccinationRecords;
    private ArrayList<HealthRecord> healthRecords;
}
