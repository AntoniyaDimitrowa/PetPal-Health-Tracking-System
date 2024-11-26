package com.example.petpal.configuration.security.token;

public interface IAccessTokenDecoder {
    IAccessToken decode(String accessTokenEncoded);
}
