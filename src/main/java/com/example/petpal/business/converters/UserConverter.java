package com.example.petpal.business.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.User;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.UserEntity;

import java.util.ArrayList;

public class UserConverter {
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
                .pets(user.getPets().map(petList -> PetConverter.convertFromPetsToPetEntities(petList))) // Optional Pet Conversion
                .breedHealthInfos(user.getBreedHealthInfos().map(breedInfoList ->
                        HealthConverter.convertFromBreedHealthInfosToEntities(breedInfoList))) // Optional BreedHealthInfo Conversion
                .build();
    }

    // Converts from UserEntity to User
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
                .pets(userEntity.getPets().map(petEntityList -> PetConverter.convertFromPetEntitiesToPets(petEntityList))) // Optional Pet Conversion
                .breedHealthInfos(userEntity.getBreedHealthInfos().map(breedInfoEntityList ->
                        HealthConverter.convertFromEntitiesToBreedHealthInfos(breedInfoEntityList))) // Optional BreedHealthInfo Conversion
                .build();
    }

}

