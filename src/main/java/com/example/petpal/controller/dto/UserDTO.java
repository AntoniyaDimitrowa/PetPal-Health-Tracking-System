package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.Pet;
import lombok.*;

import java.util.ArrayList;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String role;
    private Optional<String> address;
    private Optional<ArrayList<Pet>> pets;
    private Optional<ArrayList<BreedHealthInfo>> breedHealthInfos;
}
