package com.example.petpal.business.domain;

import com.example.petpal.business.domain.enums.Gender;
import lombok.*;

import java.util.Date;
import java.util.List;

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
    private List<VaccinationRecord> vaccinationRecords;
    private List<HealthRecord> healthRecords;
}
