package com.example.petpal.controller.dto.vaccination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class CreateVaccinationRecordDTO {
    private Long petId;
    private Long vaccinationId;
    private Date date;
}
