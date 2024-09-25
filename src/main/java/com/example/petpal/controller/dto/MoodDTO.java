package com.example.petpal.controller.dto;

import com.example.petpal.business.domain.Image;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoodDTO {
    private String name;
    private Image image;
}
