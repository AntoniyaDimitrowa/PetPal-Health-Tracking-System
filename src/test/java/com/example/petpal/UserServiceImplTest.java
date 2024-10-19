package com.example.petpal;

import com.example.petpal.persistence.converters.UserConverter;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.business.impl.UserServiceImpl;
import com.example.petpal.persistence.IUserRepository;
import com.example.petpal.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = UserEntity.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role("USER")
                .memberSince(new java.util.Date())
                .address("1234 Main St, Hometown")
                .pets(new ArrayList<>())
                .breedHealthInfos(new ArrayList<>())
                .build();
        user = UserConverter.convertFromUserEntityToUser(userEntity);
    }

    @Test
    void getUserById_shouldReturnUserWhenExists() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(userEntity));

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
    void createUser_shouldReturnCreatedUser() {
        when(userRepository.createUser(any(UserEntity.class))).thenReturn(userEntity);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).createUser(any(UserEntity.class));
    }

    @Test
    void updateUser_shouldThrowExceptionIfUserNotFound() {
        when(userRepository.getUserById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidUserException.class, () -> userService.updateUser(100L, user));
        verify(userRepository, times(1)).getUserById(100L);
    }

    @Test
    void updateUser_shouldUpdateUserWhenExists() throws InvalidUserException {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.updateUser(eq(1L), any(UserEntity.class))).thenReturn(userEntity);

        User result = userService.updateUser(1L, user);

        assertNotNull(result);
        verify(userRepository, times(1)).updateUser(eq(1L), any(UserEntity.class));
    }
}
