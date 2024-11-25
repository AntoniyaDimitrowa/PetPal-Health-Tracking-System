package com.example.petpal.business.impl;

import com.example.petpal.business.IUserService;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.configuration.security.token.IAccessToken;
import com.example.petpal.persistence.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private IAccessToken requestAccessToken;

    @Override
    public Optional<User> getUserById(Long userId) throws UnauthorizedDataAccessException {
        System.out.println("URL userId: " + userId);
        System.out.println("Token userId: " + requestAccessToken.getUserId());
        if (!Objects.equals(requestAccessToken.getUserId(), userId)) {
            throw new UnauthorizedDataAccessException();
        }

        return userRepository.getUserById(userId);
    }

    @Override
    public Long createUser(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public User updateUser(Long userId, User updatedUser) throws InvalidUserException {
        if(userRepository.getUserById(userId).isEmpty()) {
            throw new InvalidUserException(userId);
        }

        return userRepository.updateUser(userId, updatedUser);
    }

    @Override
    public boolean deleteUser(Long userId) {
        return userRepository.deleteUser(userId);
    }
}
