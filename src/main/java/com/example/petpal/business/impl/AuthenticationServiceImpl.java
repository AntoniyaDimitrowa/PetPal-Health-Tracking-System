package com.example.petpal.business.impl;

import com.example.petpal.business.IAuthenticationService;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidCredentialsException;
import com.example.petpal.configuration.security.token.IAccessTokenEncoder;
import com.example.petpal.configuration.security.token.impl.AccessTokenImpl;
import com.example.petpal.controller.dto.LoginDTO;
import com.example.petpal.controller.dto.AuthResponse;
import com.example.petpal.persistence.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IAccessTokenEncoder accessTokenEncoder;

    @Override
    public AuthResponse login(LoginDTO loginRequest) throws InvalidCredentialsException {
        User user = userRepository.getUserByEmail(loginRequest.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!matchesPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return AuthResponse.builder().accessToken(accessToken).build();
    }

    @Override
    public AuthResponse signup(User user) throws InvalidCredentialsException {
        if (userRepository.getUserByEmail(user.getEmail()).isPresent()) {
            throw new InvalidCredentialsException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Long userId = userRepository.createUser(user);

        User newUser = userRepository.getUserById(userId).get();
        String accessToken = generateAccessToken(newUser);

        return AuthResponse.builder().accessToken(accessToken).build();
    }


    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(User user) {
        Long userId = user.getId();
        String role = user.getRole();

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getEmail(), userId, Set.of(role)));
    }
}
