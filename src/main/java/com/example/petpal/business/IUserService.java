package com.example.petpal.business;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidCredentialsException;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;

import java.util.Optional;

public interface IUserService {
    Optional<User> getUserById(Long userId) throws UnauthorizedDataAccessException;
    Long createUser(User user);
    User updateUser(Long userId, String oldPassword, User updatedUser) throws InvalidUserException, InvalidCredentialsException;
    boolean deleteUser(Long userId);
}
