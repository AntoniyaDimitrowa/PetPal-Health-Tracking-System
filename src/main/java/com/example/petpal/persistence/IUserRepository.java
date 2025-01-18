package com.example.petpal.persistence;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidCredentialsException;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> getUserById(long userId);
    Optional<User> getUserByPetId(long petId);
    Optional<User> getUserByEmail(String email);
    Long createUser(User user);
    User updateUser(long userId, User updatedUser, String oldPassword) throws InvalidCredentialsException;
    boolean deleteUser(long userId);
}
