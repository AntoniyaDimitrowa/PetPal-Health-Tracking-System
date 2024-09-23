package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.Emoji;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class MoodEntity {
    private long id;
    private String name;
    private Emoji emoji;
}
