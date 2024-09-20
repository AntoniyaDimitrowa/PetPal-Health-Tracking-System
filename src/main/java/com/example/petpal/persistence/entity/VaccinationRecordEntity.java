package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.Vaccination;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationRecordEntity {
    private long id;
    private VaccinationEntity vaccination;
    private Date date;

    private PetEntity pet;
}
