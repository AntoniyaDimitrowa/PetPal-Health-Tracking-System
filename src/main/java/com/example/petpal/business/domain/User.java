package com.example.petpal.business.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
public class User {
    @Getter
    protected long id;

    @Getter
    @Setter
    protected String name;

    @Getter
    @Setter
    protected String email;

    @Getter
    @Setter
    protected String password;

    @Getter
    @Setter
    private Date memberSince;

    @Getter
    @Setter
    protected String role;
}
