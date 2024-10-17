package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.enums.VaccinationType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class VaccinationEntity {
    private Long id;
    private String name;
    private VaccinationType type;
    private int range;
}
