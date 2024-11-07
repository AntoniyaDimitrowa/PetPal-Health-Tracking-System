package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.User;
import com.example.petpal.persistence.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Optional;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConverter {

    public static UserEntity convertFromUserToUserEntity(User user) {
        if(user == null) return null;

        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .memberSince(user.getMemberSince())
                .role(user.getRole())
                .address(user.getAddress())
                .image(user.getImage())
                .pets(user.getPets().map(PetConverter::convertFromPetsToPetEntities).orElse(new ArrayList<>()))
                .breedHealthInfos(user.getBreedHealthInfos().map(HealthConverter::convertFromBreedHealthInfosToEntities).orElse(new ArrayList<>()))
                .build();
    }

    // Convert from UserEntity to User
    public static User convertFromUserEntityToUser(UserEntity userEntity) {
        if(userEntity == null) return null;
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
