package com.example.petpal.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginDTO {
    @NotNull(message = "Email is required.")
    @Email(message = "The email should be valid.")
    private String email;
    @NotNull(message = "Password is required.")
    @Size(min = 1, message = "Password must not be empty.")
    private String password;
}
