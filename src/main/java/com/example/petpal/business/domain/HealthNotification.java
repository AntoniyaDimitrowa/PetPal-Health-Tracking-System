package com.example.petpal.business.domain;

import lombok.*;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthNotification {

    private Long id;
    private Date date;
    private String message;
    private User user;
    private Pet pet;
    private boolean isRead;
}
