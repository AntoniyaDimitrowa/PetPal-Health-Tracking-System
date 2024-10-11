package com.example.petpal.controller.dto.mood;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMoodDTO {
    private String name;
    private byte[] emoji;
}
