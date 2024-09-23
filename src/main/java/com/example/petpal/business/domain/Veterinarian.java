package com.example.petpal.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class Veterinarian extends User {
    public Veterinarian(long id, String name, String email, String password, Date memberSince) {
        super(id, name, email, password, memberSince, "Veterinarian");
    }
}
