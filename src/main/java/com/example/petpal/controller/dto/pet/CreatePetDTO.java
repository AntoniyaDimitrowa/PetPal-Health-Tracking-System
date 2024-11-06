package com.example.petpal.controller.dto.pet;

import com.example.petpal.business.domain.enums.Gender;
import lombok.*;

import java.util.Date;
import java.util.List;

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

    private List<Long> vaccinationRecordsIds;
}
