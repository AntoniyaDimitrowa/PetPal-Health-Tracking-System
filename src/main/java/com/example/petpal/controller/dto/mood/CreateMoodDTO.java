package com.example.petpal.controller.dto.mood;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateMoodDTO {
    private String name;
    private String emoji;
}
