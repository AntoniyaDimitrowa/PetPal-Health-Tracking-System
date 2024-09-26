package com.example.petpal.controller.converters;

import com.example.petpal.controller.converters.HealthConverter;
import com.example.petpal.controller.converters.PetConverter;
import com.example.petpal.business.domain.User;
import com.example.petpal.controller.dto.RegisterDTO;
import com.example.petpal.controller.dto.UserDTO;
import com.example.petpal.persistence.entity.UserEntity;

import java.util.Date;

public class UserConverter {
    public static UserDTO convertFromUserToUserDTO(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .memberSince(user.getMemberSince())
                .role(user.getRole())
                .address(user.getAddress())
                .pets(user.getPets().map(petList -> PetConverter.convertFromPetsToPetDTOs(petList)))
                .breedHealthInfos(user.getBreedHealthInfos().map(breedInfoList ->
                        HealthConverter.convertFromBreedHealthInfosToDTOs(breedInfoList)))
                .build();
    }

    // Converts from UserEntity to User
    public static User convertFromUserDTOToUser(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .memberSince(userDTO.getMemberSince())
                .role(userDTO.getRole())
                .address(userDTO.getAddress())
                .pets(userDTO.getPets().map(petDTOList -> PetConverter.convertFromPetDTOsToPets(petDTOList)))
                .breedHealthInfos(userDTO.getBreedHealthInfos().map(breedInfoDTOList ->
                        HealthConverter.convertFromDTOsToBreedHealthInfos(breedInfoDTOList)))
                .build();
    }
    public static User convertFromRegisterDTOToUser(RegisterDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .memberSince(new Date())
                .role("Owner")
                .build();
    }
}

