package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.enums.VaccinationType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationEntity {
    private long id;
    private String name;
    private VaccinationType type;

    private int range;
}
