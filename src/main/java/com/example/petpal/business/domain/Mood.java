package com.example.petpal.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Mood {
    private long id;
    private String name;
    private Image image;
}
