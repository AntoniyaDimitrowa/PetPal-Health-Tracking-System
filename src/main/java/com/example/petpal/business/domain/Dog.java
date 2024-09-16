package com.example.petpal.business.domain;

import java.util.ArrayList;
import java.util.Date;

public class Dog {
    private long id;
    private String name;
    private String breed;
    private Date birthdate;
    private Owner owner;
    private ArrayList<HealthRecord> healthRecords;
}
