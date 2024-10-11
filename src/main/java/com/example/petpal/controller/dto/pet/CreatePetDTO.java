package com.example.petpal.controller.dto.pet;

import com.example.petpal.business.domain.enums.Gender;
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
    private byte[] image;

    private ArrayList<Long> vaccinationRecordsIds;
}
