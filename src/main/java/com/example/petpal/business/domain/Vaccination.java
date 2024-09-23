package com.example.petpal.business.domain;

import com.example.petpal.business.domain.enums.VaccinationType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vaccination {
    private long id;
    private String name;
    private VaccinationType type;
    private int range;
}
