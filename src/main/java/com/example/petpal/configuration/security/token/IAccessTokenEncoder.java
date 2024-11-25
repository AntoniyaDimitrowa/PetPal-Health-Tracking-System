package com.example.petpal.configuration.security.token;

public interface IAccessTokenEncoder {
    String encode(IAccessToken accessToken);
}
