package com.example.petpal.business.impl;

import com.example.petpal.business.IUserService;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.persistence.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public Long createUser(User user) {
        Long userId = userRepository.createUser(user);
        return userId;
    }

    @Override
    public User updateUser(Long userId, User updatedUser) throws InvalidUserException {
        Optional<User> userOptional = userRepository.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new InvalidUserException(userId);
        }
        return userRepository.updateUser(userId, updatedUser);
    }

    @Override
    public boolean deleteUser(Long userId) {
        return userRepository.deleteUser(userId);
    }
}
