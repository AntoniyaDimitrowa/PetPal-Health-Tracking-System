package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.Vaccination;
import lombok.*;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
public class VaccinationRecordEntity {
    private long id;
    private VaccinationEntity vaccination;
    private Date date;

    private PetEntity pet;
}
