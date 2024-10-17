package com.example.petpal.business.domain;

import lombok.*;

import java.sql.Timestamp;
@Data
@Builder
@AllArgsConstructor
public class Message {
    @Setter(AccessLevel.NONE)
    private long id;
    private User sender;
    private String content;
    private Timestamp timestamp;
}
