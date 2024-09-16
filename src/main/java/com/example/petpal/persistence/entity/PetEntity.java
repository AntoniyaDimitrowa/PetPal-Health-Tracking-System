package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.Owner;
import com.example.petpal.business.domain.VaccinationRecord;

import java.util.ArrayList;
import java.util.Date;

public class PetEntity {
    private long id;
    private String name;
    private String breed;
    private Date birthdate;
    private ArrayList<VaccinationRecordEntity> vaccinationRecords;
    private ArrayList<HealthRecordEntity> healthRecords;
}
