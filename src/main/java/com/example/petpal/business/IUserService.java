package com.example.petpal.business;

import com.example.petpal.business.domain.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> getUserById(long userId);
    User createUser(User user);
    User updateUser(long userId, User updatedUser);
    boolean deleteUser(long userId);
}
