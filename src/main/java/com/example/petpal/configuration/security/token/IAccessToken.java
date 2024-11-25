package com.example.petpal.configuration.security.token;

import java.util.Set;

public interface IAccessToken {
    String getSubject();

    Long getUserId();

    Set<String> getRoles();

    boolean hasRole(String roleName);
}
