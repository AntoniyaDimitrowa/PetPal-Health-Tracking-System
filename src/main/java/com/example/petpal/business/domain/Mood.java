package com.example.petpal.business.domain;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class Mood {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private String emoji;
}
