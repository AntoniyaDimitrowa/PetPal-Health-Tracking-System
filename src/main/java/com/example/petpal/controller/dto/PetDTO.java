package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.Owner;
import com.example.petpal.business.domain.VaccinationRecord;
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
public class PetDTO {
    private String name;
    private BreedDTO breed;
    private Gender gender;
    private Date birthdate;
    private double weight;
    private ArrayList<VaccinationRecordDTO> vaccinationRecords;
}
