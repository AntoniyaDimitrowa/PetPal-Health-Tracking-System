package com.example.petpal.controller.dto.vaccination;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class VaccinationRecordDTO {
    private VaccinationDTO vaccination;
    private Date date;
}
