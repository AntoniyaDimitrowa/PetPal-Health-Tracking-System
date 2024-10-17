package com.example.petpal.business.domain;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class Mood {
    @Setter(AccessLevel.NONE)
    private long id;
    private String name;
    private String emoji;
}
