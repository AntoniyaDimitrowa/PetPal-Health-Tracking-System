package com.example.petpal.controller.dto.mood;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class MoodDTO {
    private Long id;
    private String name;
    private String emoji;
}
