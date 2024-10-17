package com.example.petpal.business.domain;

import com.example.petpal.business.domain.enums.VaccinationType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Vaccination {
    @Setter(AccessLevel.NONE)
    private long id;
    private String name;
    private VaccinationType type;
    private int range;
}
