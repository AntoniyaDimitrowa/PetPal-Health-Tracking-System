package com.example.petpal.controller.dto.user;

import com.example.petpal.controller.dto.health.BreedHealthInfoDTO;
import com.example.petpal.controller.dto.pet.PetDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private Date memberSince;
    private String role;
    private Optional<String> address;
    private Optional<ArrayList<PetDTO>> pets;
    private Optional<ArrayList<BreedHealthInfoDTO>> breedHealthInfos;
}
