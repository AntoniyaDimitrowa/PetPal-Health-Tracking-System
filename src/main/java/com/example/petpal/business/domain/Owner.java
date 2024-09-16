package com.example.petpal.business.domain;

import java.util.ArrayList;
import java.util.Date;

public class Owner extends User {
    private Date memberSince;
    private String address;
    private ArrayList<Pet> pets;

    public Owner(long id, String name, String email, String password, String address, Date memberSince) {
        super(id, name, email, password, "Owner");
        this.address = address;
        this.memberSince = memberSince;
        this.pets = new ArrayList<>();
    }
}
