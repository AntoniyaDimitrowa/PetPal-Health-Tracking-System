package com.example.petpal.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterDTO {
    private String name;
    private String email;
    private String password;
    private String repeatPassword;
}
