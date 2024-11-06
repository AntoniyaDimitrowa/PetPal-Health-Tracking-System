package com.example.petpal.business.impl;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.persistence.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks before each test
    }

    @Test
    void getUserById_shouldReturnUserWhenExists() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_shouldReturnEmptyWhenNotFound() {
        when(userRepository.getUserById(100L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(100L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).getUserById(100L);
    }

    @Test
    void createUser_shouldReturnUserIdWhenCreated() {
        when(userRepository.createUser(any(User.class))).thenReturn(1L);
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));

        Long result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(1L, result);  // Check if the returned userId is correct
        verify(userRepository, times(1)).createUser(any(User.class));
    }

    @Test
    void updateUser_shouldThrowExceptionIfUserNotFound() {
        when(userRepository.getUserById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidUserException.class, () -> userService.updateUser(100L, user));
        verify(userRepository, times(1)).getUserById(100L);
    }

    @Test
    void updateUser_shouldUpdateUserWhenExists() throws InvalidUserException {
        User updatedUser = User.builder().id(1L).name("John Updated").email("updated@example.com").build();
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(userRepository.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("John Updated", result.getName());  // Check if name was updated
        assertEquals("updated@example.com", result.getEmail());  // Check if email was updated
        verify(userRepository, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    void deleteUser_shouldReturnTrueWhenDeleted() {
        when(userRepository.deleteUser(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_shouldReturnFalseWhenNotDeleted() {
        when(userRepository.deleteUser(100L)).thenReturn(false);

        boolean result = userService.deleteUser(100L);

        assertFalse(result);
        verify(userRepository, times(1)).deleteUser(100L);
    }
}
