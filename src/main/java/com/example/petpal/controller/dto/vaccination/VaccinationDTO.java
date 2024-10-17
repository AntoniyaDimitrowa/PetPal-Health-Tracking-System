package com.example.petpal.controller.dto.vaccination;

import com.example.petpal.business.domain.enums.VaccinationType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class VaccinationDTO {
    private long id;
    private String name;
    private VaccinationType type;
    private int range;
}
