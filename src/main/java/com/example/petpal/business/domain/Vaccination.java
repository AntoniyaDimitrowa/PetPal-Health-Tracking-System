package com.example.petpal.business.domain;

import com.example.petpal.business.domain.enums.VaccinationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Vaccination {
    private long id;
    private String name;
    private VaccinationType type;
    private int range;
}
