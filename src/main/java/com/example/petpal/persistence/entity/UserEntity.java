package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.Pet;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class UserEntity {
    protected long id;

    protected String name;

    protected String email;

    protected String password;

    private Date memberSince;

    protected String role;

    private String address;

    private String image;

    private Optional<ArrayList<PetEntity>> pets;

    private Optional<ArrayList<BreedHealthInfoEntity>> breedHealthInfos;

}
