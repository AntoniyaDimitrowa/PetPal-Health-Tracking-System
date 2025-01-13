package com.example.petpal.controller.dto.vaccination;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class CreateVaccinationRecordDTO {
    @NotNull(message = "Pet is required.")
    private Long petId;
    @NotNull(message = "Vaccination is required.")
    private Long vaccinationId;
    @NotNull(message = "Date is required.")
    private Date date;
}
