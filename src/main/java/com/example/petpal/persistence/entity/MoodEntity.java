package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoodEntity {
    private long id;
    private String name;
    private Image image;
}
