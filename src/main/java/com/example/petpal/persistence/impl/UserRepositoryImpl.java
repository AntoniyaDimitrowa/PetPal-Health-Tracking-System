package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.User;
import com.example.petpal.persistence.IUserRepository;
import com.example.petpal.persistence.IUserRepositoryJPA;
import com.example.petpal.persistence.converters.UserConverter;
import com.example.petpal.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements IUserRepository {

    private final IUserRepositoryJPA userRepositoryJPA;

    @Autowired
    public UserRepositoryImpl(IUserRepositoryJPA userRepositoryJPA) {
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        Optional<UserEntity> userEntityOpt = userRepositoryJPA.findById(userId);
        return userEntityOpt.map(UserConverter::convertFromUserEntityToUser);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<UserEntity> userEntityOpt = userRepositoryJPA.findByEmail(email);
        return userEntityOpt.map(UserConverter::convertFromUserEntityToUser);
    }

    @Override
    public Long createUser(User user) {
        UserEntity userEntity = UserConverter.convertFromUserToUserEntity(user);
        UserEntity savedEntity = userRepositoryJPA.save(userEntity);
        return savedEntity.getId();
    }

    @Override
    public User updateUser(long userId, User updatedUser) {
        Optional<UserEntity> existingUserOpt = userRepositoryJPA.findById(userId);
        if (existingUserOpt.isPresent()) {
            UserEntity existingUser = existingUserOpt.get();
            UserEntity updatedUserEntity = UserConverter.convertFromUserToUserEntity(updatedUser);
            updatedUserEntity.setId(existingUser.getId()); // Keep the same ID
            UserEntity savedEntity = userRepositoryJPA.save(updatedUserEntity);
            return UserConverter.convertFromUserEntityToUser(savedEntity);
        }
        return null;
    }

    @Override
    public boolean deleteUser(long userId) {
        if (userRepositoryJPA.existsById(userId)) {
            userRepositoryJPA.deleteById(userId);
            return true;
        }
        return false;
    }
}
