package com.example.petpal.business.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    protected long id;

    protected String name;

    protected String email;

    protected String password;

    private Date memberSince;

    protected String role;

    private Optional<String> address;
    private Optional<ArrayList<Pet>> pets;
    private Optional<ArrayList<BreedHealthInfo>> breedHealthInfos;
}
