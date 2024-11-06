package com.example.petpal.controller.dto.user;

import com.example.petpal.controller.dto.health.BreedHealthInfoDTO;
import com.example.petpal.controller.dto.pet.PetDTO;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Date memberSince;
    private String role;
    private String address;
    private Optional<List<PetDTO>> pets;
    private Optional<List<BreedHealthInfoDTO>> breedHealthInfos;
    private String image;
}
