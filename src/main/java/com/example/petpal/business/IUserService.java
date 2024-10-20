package com.example.petpal.business;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidUserException;

import java.util.ArrayList;
import java.util.Optional;

public interface IUserService {
    Optional<User> getUserById(long userId);
    Long createUser(User user);
    User updateUser(long userId, User updatedUser) throws InvalidUserException;
    boolean deleteUser(long userId);
}
