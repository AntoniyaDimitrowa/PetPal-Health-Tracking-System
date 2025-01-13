package com.example.petpal.controller.dto.mood;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateMoodDTO {
    @NotNull(message = "Name is required.")
    @Size(min = 1, message = "Name must not be empty.")
    private String name;

    @NotNull(message = "Emoji is required.")
    @Size(min = 1, message = "Emoji must not be empty.")
    private String emoji;
}
