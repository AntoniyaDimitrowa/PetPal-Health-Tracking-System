package com.example.petpal.persistence.impl;

import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IUserRepository;
import com.example.petpal.persistence.entity.PetEntity;
import com.example.petpal.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements IUserRepository {
    private final ArrayList<UserEntity> users = new ArrayList<>();
    private static long nextUserId = 1L;

    public UserRepositoryImpl(IPetRepository petRepo) {
        // Add some example users to the list
        users.add(UserEntity.builder()
                .id(nextUserId++)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role("USER")
                .memberSince(new java.util.Date())
                .address("1234 Main St, Hometown")
                .pets(Optional.of(new ArrayList<PetEntity>() {
                    {
                        add(petRepo.getPet(1L).get());
                    }
                }))
                .breedHealthInfos(Optional.empty())
                .build());

        users.add(UserEntity.builder()
                .id(nextUserId++)
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .password("password456")
                .role("ADMIN")
                .memberSince(new java.util.Date())
                .address("5678 Market St, Cityville")
                .pets(Optional.empty())
                .breedHealthInfos(Optional.empty())
                .build());
    }

    @Override
    public Optional<UserEntity> getUserById(long userId) {
        return users.stream().filter(user -> user.getId() == userId).findFirst();
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        user.setId(nextUserId++);
        users.add(user);
        return user;
    }

    @Override
    public UserEntity updateUser(long userId, UserEntity updatedUser) {
        Optional<UserEntity> existingUserOpt = getUserById(userId);
        if (existingUserOpt.isPresent()) {
            UserEntity existingUser = existingUserOpt.get();
            users.remove(existingUser);
            updatedUser.setId(userId); // Keep the same ID
            users.add(updatedUser);
            return updatedUser;
        }
        return null;
    }

    @Override
    public boolean deleteUser(long userId) {
        return users.removeIf(user -> user.getId() == userId);
    }
}
