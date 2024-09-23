package com.example.petpal.controller.dto;

import lombok.*;

import java.util.ArrayList;
@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String role;

    //private ArrayList<PetDTO> pets; ??
}
