package com.example.petpal.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class UpdateUserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String image;
}
