package com.example.petpal.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
@Data
@Builder
public class Owner extends User {
    private String address;
    private ArrayList<Pet> pets;

    public Owner(long id, String name, String email, String password, String address, Date memberSince) {
        super(id, name, email, password, memberSince, "Owner");
        this.address = address;
        this.pets = new ArrayList<>();
    }
}
