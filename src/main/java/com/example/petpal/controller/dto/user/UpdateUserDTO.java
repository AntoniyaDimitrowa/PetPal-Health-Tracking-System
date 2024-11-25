package com.example.petpal.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class UpdateUserDTO {
    private String name;
    private String email;
    private String oldPassword;
    private String newPassword;
    private String address;
    private String image;
}
