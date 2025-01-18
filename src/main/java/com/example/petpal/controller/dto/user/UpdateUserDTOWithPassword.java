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
public class UpdateUserDTOWithPassword {
    @NotNull(message = "Name is required.")
    @Size(min = 1, message = "Name must not be empty.")
    private String name;

    @NotNull(message = "Email is required.")
    @Email(message = "The email should be valid.")
    private String email;

    @NotNull(message = "Old Password is required.")
    @Size(min = 10, message = "Old Password must be at least 10 characters.")
    private String oldPassword;

    @NotNull(message = "New Password is required.")
    @Size(min = 10, message = "New Password must be at least 10 characters.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$",
            message = "New Password must be at least 10 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    private String newPassword;

    @NotNull(message = "Address is required.")
    @Size(min = 1, message = "Address must not be empty.")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z\\s-]{0,99},\\s[A-Za-z][A-Za-z\\s-]{0,99}$",
            message = "Address must be in the format 'City, Country'. Each part (City or Country) can be up to 100 characters."
    )
    private String address;

    private String image; // Optional image in Base64 format
}
