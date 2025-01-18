package com.example.petpal.business.impl;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidCredentialsException;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.configuration.security.token.IAccessToken;
import com.example.petpal.persistence.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private IAccessToken requestAccessToken;

    @Mock
    private PasswordEncoder passwordEncoder;

    private static final User user = User.builder()
            .id(1L)
            .name("John Doe")
            .email("john.doe@example.com")
            .password("password123")
            .role("USER")
            .memberSince(new java.util.Date())
            .address("1234 Main St, Hometown")
            .pets(Optional.empty())
            .breedHealthInfos(Optional.empty())
            .image("image_url")
            .build();

    private static final User updatedUser = User.builder()
            .id(1L)
            .name("John Updated")
            .email("updated@example.com")
            .address("Updated Address")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_shouldReturnUserWhenAuthorized() throws UnauthorizedDataAccessException {
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_shouldThrowUnauthorizedDataAccessExceptionWhenUnauthorized() {
        when(requestAccessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () -> userService.getUserById(1L));
        verify(userRepository, never()).getUserById(1L);
    }

    @Test
    void createUser_shouldReturnUserIdWhenCreated() {
        when(userRepository.createUser(any(User.class))).thenReturn(1L);

        Long result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(1L, result);
        verify(userRepository, times(1)).createUser(any(User.class));
    }

    @Test
    void updateUser_shouldUpdateSuccessfullyWhenValid() throws Exception {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);
        when(userRepository.updateUser(1L, updatedUser, "password123")).thenReturn(updatedUser);

        User result = userService.updateUser(1L, "password123", updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser, result);
        verify(userRepository, times(1)).updateUser(1L, updatedUser, "password123");
    }

    @Test
    void updateUser_shouldThrowInvalidUserExceptionIfUserNotFound() {
        when(userRepository.getUserById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidUserException.class, () -> userService.updateUser(100L, "oldPassword", updatedUser));
        verify(userRepository, times(1)).getUserById(100L);
    }

    @Test
    void updateUser_shouldThrowInvalidCredentialsExceptionIfPasswordMismatch() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(passwordEncoder.matches("wrongOldPassword", user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.updateUser(1L, "wrongOldPassword", updatedUser));
    }

    @Test
    void updateUser_shouldThrowUnauthorizedDataAccessExceptionIfUnauthorized() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(requestAccessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () -> userService.updateUser(1L, "password123", updatedUser));
    }

    @Test
    void updateUserWithoutPassword_shouldUpdateSuccessfullyWithoutPassword() throws Exception {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(userRepository.updateUser(1L, updatedUser, null)).thenReturn(updatedUser);

        User result = userService.updateUserWithoutPassword(1L, updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser, result);
        verify(userRepository, times(1)).updateUser(1L, updatedUser, null);
    }

    @Test
    void updateUserWithoutPassword_shouldThrowInvalidUserExceptionIfUserNotFound() {
        when(userRepository.getUserById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidUserException.class, () -> userService.updateUserWithoutPassword(100L, updatedUser));
        verify(userRepository, times(1)).getUserById(100L);
    }

    @Test
    void updateUserWithoutPassword_shouldThrowUnauthorizedDataAccessExceptionIfUnauthorized() throws InvalidCredentialsException {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(requestAccessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () -> userService.updateUserWithoutPassword(1L, updatedUser));
        verify(userRepository, never()).updateUser(eq(1L), any(User.class), anyString());
    }

    @Test
    void deleteUser_shouldReturnTrueWhenDeletedSuccessfully() throws UnauthorizedDataAccessException {
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(userRepository.deleteUser(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_shouldThrowUnauthorizedDataAccessExceptionWhenUnauthorized() {
        when(requestAccessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteUser(1L);
    }

    @Test
    void getUserByPetId_shouldReturnUserWhenFound() {
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByPetId(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).getUserByPetId(1L);
    }

    @Test
    void getUserByPetId_shouldReturnEmptyOptionalWhenNotFound() {
        when(userRepository.getUserByPetId(2L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByPetId(2L);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).getUserByPetId(2L);
    }
}
