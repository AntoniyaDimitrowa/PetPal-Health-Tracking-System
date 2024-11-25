package com.example.petpal.persistence;

import com.example.petpal.business.domain.User;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> getUserById(long userId);
    Optional<User> getUserByEmail(String email);
    Long createUser(User user);
    User updateUser(long userId, User updatedUser);
    boolean deleteUser(long userId);
}
