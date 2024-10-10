package com.example.petpal.controller.dto.pet;

import com.example.petpal.business.domain.Image;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.controller.dto.health.HealthRecordDTO;
import com.example.petpal.controller.dto.vaccination.VaccinationRecordDTO;
import com.example.petpal.controller.dto.breed.BreedDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetDTO {
    private long id;
    private String name;
    private BreedDTO breed;
    private Gender gender;
    private Date birthdate;
    private double weight;
    private Image image;

    private ArrayList<VaccinationRecordDTO> vaccinationRecords;
    private ArrayList<HealthRecordDTO> healthRecords;

}
