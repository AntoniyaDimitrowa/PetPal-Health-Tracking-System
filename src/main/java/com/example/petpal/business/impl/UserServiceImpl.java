package com.example.petpal.business.impl;

import com.example.petpal.business.IUserService;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidCredentialsException;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.configuration.security.token.IAccessToken;
import com.example.petpal.persistence.IUserRepository;
import com.example.petpal.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private IAccessToken requestAccessToken;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserById(Long userId) throws UnauthorizedDataAccessException {
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
    public User updateUser(Long userId, String oldPassword, User updatedUser) throws InvalidUserException, InvalidCredentialsException {
        Optional<User> user = userRepository.getUserById(userId);
        if(user.isEmpty()) {
            throw new InvalidUserException(userId);
        }

        if(!passwordEncoder.matches(oldPassword, user.get().getPassword())) {
            throw new InvalidCredentialsException();
        }
        return userRepository.updateUser(userId, updatedUser);
    }

    @Override
    public boolean deleteUser(Long userId) {
        return userRepository.deleteUser(userId);
    }
}
