package com.example.petpal.business;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidCredentialsException;
import com.example.petpal.controller.dto.LoginDTO;
import com.example.petpal.controller.dto.AuthResponse;

public interface IAuthenticationService {
    AuthResponse login(LoginDTO loginRequest) throws InvalidCredentialsException;
    AuthResponse signup(User user) throws InvalidCredentialsException;

}
