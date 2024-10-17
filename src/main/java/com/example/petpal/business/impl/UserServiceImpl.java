package com.example.petpal.business.impl;

import com.example.petpal.business.IUserService;
import com.example.petpal.persistence.converters.UserConverter;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.persistence.IUserRepository;
import com.example.petpal.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    @Override
    public Optional<User> getUserById(long userId) {
        return userRepository.getUserById(userId).map(UserConverter::convertFromUserEntityToUser);
    }

    @Override
    public User createUser(User user) {
        return UserConverter.convertFromUserEntityToUser(userRepository.createUser(UserConverter.convertFromUserToUserEntity(user)));
    }

    @Override
    public User updateUser(long userId, User updatedUser) throws InvalidUserException {
        Optional<UserEntity> breedOptional = userRepository.getUserById(userId);
        if (breedOptional.isEmpty()) {
            throw new InvalidUserException(userId);
        }
        return UserConverter.convertFromUserEntityToUser(userRepository.updateUser(userId, UserConverter.convertFromUserToUserEntity(updatedUser)));
    }

    @Override
    public boolean deleteUser(long userId) {
        return userRepository.deleteUser(userId);
    }
}
