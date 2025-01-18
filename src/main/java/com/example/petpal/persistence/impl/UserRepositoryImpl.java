package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidCredentialsException;
import com.example.petpal.persistence.IUserRepository;
import com.example.petpal.persistence.IUserRepositoryJPA;
import com.example.petpal.persistence.converters.UserConverter;
import com.example.petpal.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements IUserRepository {

    private final IUserRepositoryJPA userRepositoryJPA;
    private final PasswordEncoder passwordEncoder;

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
    public Optional<User> getUserByPetId(long petId) {
        Optional<UserEntity> userEntityOpt = userRepositoryJPA.findByPetId(petId);
        return userEntityOpt.map(UserConverter::convertFromUserEntityToUser);
    }

    @Override
    public Long createUser(User user) {
        UserEntity userEntity = UserConverter.convertFromUserToUserEntity(user);
        UserEntity savedEntity = userRepositoryJPA.save(userEntity);
        return savedEntity.getId();
    }

    @Override
    public User updateUser(long userId, User updatedUser, String oldPassword) throws InvalidCredentialsException {
        Optional<UserEntity> existingUserOpt = userRepositoryJPA.findById(userId);

        if (existingUserOpt.isPresent()) {
            UserEntity existingUser = existingUserOpt.get();

            // If the password is being updated, check if the old password matches
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                // Assuming you have a method to verify the old password.
                if(!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
                    throw new InvalidCredentialsException();
                }
                // Here you would hash the new password, depending on your security setup
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            // Update other user fields
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setImage(updatedUser.getImage());

            // Save the updated entity
            UserEntity savedEntity = userRepositoryJPA.save(existingUser);
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
