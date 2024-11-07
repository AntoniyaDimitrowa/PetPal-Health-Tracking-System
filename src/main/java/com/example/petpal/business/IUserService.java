package com.example.petpal.business;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidUserException;

import java.util.Optional;

public interface IUserService {
    Optional<User> getUserById(Long userId);
    Long createUser(User user);
    User updateUser(Long userId, User updatedUser) throws InvalidUserException;
    boolean deleteUser(Long userId);
}
