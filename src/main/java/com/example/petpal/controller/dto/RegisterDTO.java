package com.example.petpal.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterDTO {
    @NotNull(message = "Name is required.")
    @Size(min = 1, message = "Name must not be empty.")
    private String name;
    @NotNull(message = "Email is required.")
    @Email(message = "The email should be valid.")
    private String email;
    @NotNull(message = "Address is required.")
    @Size(min = 1, message = "Address must not be empty.")
    @Pattern(
            regexp = "^[A-Za-z]+(?:[\\s-][A-Za-z]+)*,\\s[A-Za-z]+(?:[\\s-][A-Za-z]+)*$",
            message = "Address must be in the format 'City, Country'."
    )
    private String address;
    @NotNull(message = "Password is required.")
    @Size(min = 1, message = "Password must not be empty.")
    private String password;
}
