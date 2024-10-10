package com.example.petpal.controller.dto.vaccination;

import com.example.petpal.controller.dto.vaccination.VaccinationDTO;
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
