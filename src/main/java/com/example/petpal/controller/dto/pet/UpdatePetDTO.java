package com.example.petpal.controller.dto.pet;

import com.example.petpal.business.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class UpdatePetDTO {
    private Long id;
    private String name;
    private Long breedId;
    private Gender gender;
    private Date birthdate;
    private double weight;
    private String image;
}
