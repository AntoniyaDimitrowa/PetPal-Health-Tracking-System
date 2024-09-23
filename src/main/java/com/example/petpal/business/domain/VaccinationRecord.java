package com.example.petpal.business.domain;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationRecord {
    private long id;
    private Vaccination vaccination;
    private Date date;
}
