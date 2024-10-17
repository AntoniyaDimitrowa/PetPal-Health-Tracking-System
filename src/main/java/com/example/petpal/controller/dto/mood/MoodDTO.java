package com.example.petpal.controller.dto.mood;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class MoodDTO {
    private long id;
    private String name;
    private String emoji;
}
