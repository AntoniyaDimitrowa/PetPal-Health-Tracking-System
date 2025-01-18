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
public class LoginDTO {
    @NotNull(message = "Email is required.")
    @Email(message = "The email should be valid.")
    private String email;
    @NotNull(message = "Password is required.")
    @Size(min = 10, message = "Password must be at least 10 characters.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,}$",
            message = "Password must be at least 10 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    private String password;
}
