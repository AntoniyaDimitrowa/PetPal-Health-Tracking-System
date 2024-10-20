package com.example.petpal.persistence;

import com.example.petpal.business.domain.User;
import com.example.petpal.persistence.entity.UserEntity;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> getUserById(long userId);
    Long createUser(User user);
    User updateUser(long userId, User updatedUser);
    boolean deleteUser(long userId);
}
