package com.example.petpal.controller.dto.pet;

import com.example.petpal.business.domain.Image;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.controller.dto.vaccination.VaccinationRecordDTO;
import com.example.petpal.controller.dto.breed.BreedDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePetDTO {

    private String name;
    private long breedId;
    private Gender gender;
    private Date birthdate;
    private double weight;
    private Image image;

    private ArrayList<Long> vaccinationRecordsIds;
}
