package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.Vaccination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VaccinationRecordDTO {
    private VaccinationDTO vaccination;
    private Date date;
}
