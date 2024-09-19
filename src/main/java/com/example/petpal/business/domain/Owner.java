package com.example.petpal.business.domain;

import java.util.ArrayList;
import java.util.Date;

public class Owner extends User {
    private String address;
    private ArrayList<Pet> pets;

    public Owner(long id, String name, String email, String password, String address, Date memberSince) {
        super(id, name, email, password, memberSince, "Owner");
        this.address = address;
        this.pets = new ArrayList<>();
    }
}
