package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.enums.VaccinationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationDTO {
    private String name;
    private VaccinationType type;

    private int range;
}
