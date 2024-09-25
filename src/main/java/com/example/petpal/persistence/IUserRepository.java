package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.UserEntity;

import java.util.Optional;

public interface IUserRepository {
    Optional<UserEntity> getUserById(long userId);
    UserEntity createUser(UserEntity user);
    UserEntity updateUser(long userId, UserEntity updatedUser);
    boolean deleteUser(long userId);
}
