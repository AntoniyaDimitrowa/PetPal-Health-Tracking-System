package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MoodEntity {
    private long id;
    private String name;
    private Image image;
}
