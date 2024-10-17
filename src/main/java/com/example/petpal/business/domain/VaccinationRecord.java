package com.example.petpal.business.domain;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class VaccinationRecord {
    @Setter(AccessLevel.NONE)
    private long id;
    private Vaccination vaccination;
    private Date date;
}
