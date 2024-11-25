package com.example.petpal.controller;

import com.example.petpal.business.IAuthenticationService;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidCredentialsException;
import com.example.petpal.controller.converters.UserConverter;
import com.example.petpal.controller.dto.LoginDTO;
import com.example.petpal.controller.dto.AuthResponse;
import com.example.petpal.controller.dto.RegisterDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginDTO loginRequest) {
        try {
            AuthResponse loginResponse = authService.login(loginRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
        }
        catch (InvalidCredentialsException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody RegisterDTO registerDTO) {
        try {
            User user = UserConverter.convertFromRegisterDTOToUser(registerDTO);
            AuthResponse response = authService.signup(user);
            return ResponseEntity.ok(response);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
