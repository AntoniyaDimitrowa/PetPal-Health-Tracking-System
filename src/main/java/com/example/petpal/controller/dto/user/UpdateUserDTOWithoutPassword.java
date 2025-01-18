package com.example.petpal.controller.dto.user;

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
public class UpdateUserDTOWithoutPassword {
    @NotNull(message = "Name is required.")
    @Size(min = 1, message = "Name must not be empty.")
    private String name;

    @NotNull(message = "Email is required.")
    @Email(message = "The email should be valid.")
    private String email;

    @NotNull(message = "Address is required.")
    @Size(min = 1, message = "Address must not be empty.")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z\\s-]{0,99},\\s[A-Za-z][A-Za-z\\s-]{0,99}$",
            message = "Address must be in the format 'City, Country'. Each part (City or Country) can be up to 100 characters."
    )
    private String address;

    private String image; // Optional image in Base64 format
}
