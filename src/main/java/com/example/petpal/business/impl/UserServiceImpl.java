package com.example.petpal.business.impl;

import com.example.petpal.business.IUserService;
import com.example.petpal.business.converters.UserConverter;
import com.example.petpal.business.domain.User;
import com.example.petpal.persistence.IUserRepository;

import java.util.Optional;

public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(UserConverter.convertFromUserEntityToUser(userRepository.getUserById(userId).get()));
    }

    @Override
    public User createUser(User user) {
        return UserConverter.convertFromUserEntityToUser(userRepository.createUser(UserConverter.convertFromUserToUserEntity(user)));
    }

    @Override
    public User updateUser(long userId, User updatedUser) {
        return UserConverter.convertFromUserEntityToUser(userRepository.updateUser(userId, UserConverter.convertFromUserToUserEntity(updatedUser)));
    }

    @Override
    public boolean deleteUser(long userId) {
        return userRepository.deleteUser(userId);
    }
}
