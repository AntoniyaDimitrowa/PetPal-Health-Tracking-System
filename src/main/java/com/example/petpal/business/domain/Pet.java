package com.example.petpal.business.domain;

import java.util.ArrayList;
import java.util.Date;

public class Pet {
    private long id;
    private String name;
    private String breed;
    private Date birthdate;
    private ArrayList<VaccinationRecord> vaccinationRecords;
    private ArrayList<HealthRecord> healthRecords;
}
