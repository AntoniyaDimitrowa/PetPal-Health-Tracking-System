package com.example.petpal.business.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class User {
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    private String email;

    private String password;

    private Date memberSince;

    private String role;

    private String image;

    private String address;

    private Optional<ArrayList<Pet>> pets;
    private Optional<ArrayList<BreedHealthInfo>> breedHealthInfos;
}
