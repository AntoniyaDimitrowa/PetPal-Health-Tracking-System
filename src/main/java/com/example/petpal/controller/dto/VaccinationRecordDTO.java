package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.Vaccination;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationRecordDTO {
    private VaccinationDTO vaccination;
    private Date date;
}
