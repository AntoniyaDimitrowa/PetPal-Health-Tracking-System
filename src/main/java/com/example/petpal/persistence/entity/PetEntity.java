package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.Owner;
import com.example.petpal.business.domain.VaccinationRecord;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class PetEntity {
    private long id;
    private String name;
    private String breed;
    private Date birthdate;
    private ArrayList<VaccinationRecordEntity> vaccinationRecords;
    private ArrayList<HealthRecordEntity> healthRecords;
}
