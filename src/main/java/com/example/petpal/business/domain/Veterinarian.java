package com.example.petpal.business.domain;

import java.util.Date;

public class Veterinarian extends User {
    public Veterinarian(long id, String name, String email, String password, Date memberSince) {
        super(id, name, email, password, memberSince, "Veterinarian");
    }
}
