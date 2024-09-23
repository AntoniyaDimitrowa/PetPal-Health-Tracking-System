package com.example.petpal.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
@Data
@Builder
@AllArgsConstructor
public class Message {
    private User sender;
    private String content;
    private Timestamp timestamp;
}
