package com.example.petpal.controller.dto.pet;

import com.example.petpal.business.domain.enums.Gender;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class CreatePetDTO {

    private String name;
    private Long breedId;
    private Gender gender;
    private Date birthdate;
    private double weight;
    private String image;

    private ArrayList<Long> vaccinationRecordsIds;
}
