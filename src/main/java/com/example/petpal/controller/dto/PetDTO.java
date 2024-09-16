package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.Owner;

import java.util.ArrayList;
import java.util.Date;

public class PetDTO {
    private String name;
    private String breed;
    private Date birthdate;
    private Owner owner;
    private ArrayList<HealthRecordDTO> healthRecords;
}
