package com.example.petpal.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MoodEntity {
    private long id;
    private String name;
}
