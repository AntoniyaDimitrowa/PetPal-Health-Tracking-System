package com.example.petpal.business.impl;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidCredentialsException;
import com.example.petpal.controller.dto.LoginDTO;
import com.example.petpal.controller.dto.AuthResponse;
import com.example.petpal.configuration.security.token.IAccessTokenEncoder;
import com.example.petpal.persistence.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class AuthenticationServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IAccessTokenEncoder accessTokenEncoder;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private static final User user = User.builder()
            .id(1L)
            .name("John Doe")
            .email("john.doe@example.com")
            .password("password123")
            .role("USER")
            .build();

    private static final LoginDTO loginDTO = LoginDTO.builder()
            .email("john.doe@example.com")
            .password("password123")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
    void login_shouldReturnAuthResponseWhenValidCredentials() throws InvalidCredentialsException {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(true);
        String accessToken = "mockAccessToken";
        when(accessTokenEncoder.encode(any())).thenReturn(accessToken);

        AuthResponse authResponse = authenticationService.login(loginDTO);

        assertNotNull(authResponse);
        assertEquals(accessToken, authResponse.getAccessToken());
        verify(userRepository, times(1)).getUserByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).matches(loginDTO.getPassword(), user.getPassword());
        verify(accessTokenEncoder, times(1)).encode(any());
    }

    @Test
    void login_shouldThrowInvalidCredentialsExceptionWhenUserNotFound() {
        when(userRepository.getUserByEmail(loginDTO.getEmail())).thenReturn(java.util.Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(loginDTO));
        verify(userRepository, times(1)).getUserByEmail(loginDTO.getEmail());
    }

    @Test
    void login_shouldThrowInvalidCredentialsExceptionWhenPasswordDoesNotMatch() {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(loginDTO));
        verify(userRepository, times(1)).getUserByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).matches(loginDTO.getPassword(), user.getPassword());
    }

    @Test
    void signup_shouldReturnAuthResponseWhenUserIsCreated() throws InvalidCredentialsException {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.createUser(user)).thenReturn(1L);
        when(userRepository.getUserById(1L)).thenReturn(java.util.Optional.of(user));
        String accessToken = "mockAccessToken";
        when(accessTokenEncoder.encode(any())).thenReturn(accessToken);

        AuthResponse authResponse = authenticationService.signup(user);

        assertNotNull(authResponse);
        assertEquals(accessToken, authResponse.getAccessToken());
        verify(userRepository, times(1)).getUserByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).createUser(user);
        verify(userRepository, times(1)).getUserById(1L);
        verify(accessTokenEncoder, times(1)).encode(any());
    }

    @Test
    void signup_shouldThrowInvalidCredentialsExceptionWhenUserAlreadyExists() {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(java.util.Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.signup(user));
        verify(userRepository, times(1)).getUserByEmail(user.getEmail());
        verify(userRepository, never()).createUser(user);
    }
}
