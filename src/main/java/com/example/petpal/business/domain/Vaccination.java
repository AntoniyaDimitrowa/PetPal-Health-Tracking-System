package com.example.petpal.business.domain;

import com.example.petpal.business.domain.enums.VaccinationType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Vaccination {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private VaccinationType type;
    private int range;
}
