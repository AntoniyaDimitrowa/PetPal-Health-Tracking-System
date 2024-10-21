package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.User;
import com.example.petpal.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.Optional;

public class UserConverter {

    // Convert from User to UserEntity
    public static UserEntity convertFromUserToUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .memberSince(user.getMemberSince())
                .role(user.getRole())
                .address(user.getAddress())
                .image(user.getImage())
                .pets(user.getPets() != null ? PetConverter.convertFromPetsToPetEntities(user.getPets().get()) : new ArrayList<>())
                .breedHealthInfos(user.getBreedHealthInfos() != null ?
                        HealthConverter.convertFromBreedHealthInfosToEntities(user.getBreedHealthInfos().get()) : new ArrayList<>())
                .build();
    }

    // Convert from UserEntity to User
    public static User convertFromUserEntityToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .memberSince(userEntity.getMemberSince())
                .role(userEntity.getRole())
                .address(userEntity.getAddress())
                .image(userEntity.getImage())
                .pets(userEntity.getPets() != null ?
                        Optional.of(PetConverter.convertFromPetEntitiesToPets(userEntity.getPets())) : Optional.empty())
                .breedHealthInfos(userEntity.getBreedHealthInfos() != null ?
                        Optional.of(HealthConverter.convertFromEntitiesToBreedHealthInfos(userEntity.getBreedHealthInfos())) : Optional.empty())
                .build();
    }

}
