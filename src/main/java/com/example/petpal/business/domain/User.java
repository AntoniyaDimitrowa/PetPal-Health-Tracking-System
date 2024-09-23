package com.example.petpal.business.domain;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class User {
    protected long id;

    protected String name;

    protected String email;

    protected String password;

    private Date memberSince;

    protected String role;
}
