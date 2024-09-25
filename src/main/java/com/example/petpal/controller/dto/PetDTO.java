package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.Image;
import com.example.petpal.business.domain.enums.Gender;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetDTO {
    private String name;
    private BreedDTO breed;
    private Gender gender;
    private Date birthdate;
    private double weight;
    private Image image;

    private ArrayList<VaccinationRecordDTO> vaccinationRecords;
}
