package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.User;
import com.example.petpal.controller.dto.RegisterDTO;
import com.example.petpal.controller.dto.user.UpdateUserDTO;
import com.example.petpal.controller.dto.user.UpdateUserDTOWithPassword;
import com.example.petpal.controller.dto.user.UpdateUserDTOWithoutPassword;
import com.example.petpal.controller.dto.user.UserDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class UserConverter {

    public static UserDTO convertFromUserToUserDTO(User user) {
        if (user == null) return null;
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .memberSince(user.getMemberSince())
                .role(user.getRole())
                .address(user.getAddress())
                .image(user.getImage())
                .pets(Optional.of(user.getPets().map(PetConverter::convertFromPetsToPetDTOs).orElse(new ArrayList<>())))
                .breedHealthInfos(Optional.of(user.getBreedHealthInfos().map(HealthConverter::convertFromBreedHealthInfosToDTOs).orElse(new ArrayList<>())))
                .build();
    }

    public static User convertFromUserDTOToUser(UserDTO userDTO) {
        if (userDTO == null) return null;
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .memberSince(userDTO.getMemberSince())
                .role(userDTO.getRole())
                .address(userDTO.getAddress())
                .image(userDTO.getImage())
                .pets(Optional.of(userDTO.getPets().map(PetConverter::convertFromPetDTOsToPets).orElse(new ArrayList<>())))
                .breedHealthInfos(Optional.of(userDTO.getBreedHealthInfos().map(HealthConverter::convertFromDTOsToBreedHealthInfos).orElse(new ArrayList<>())))
                .build();
    }

    public static User convertFromRegisterDTOToUser(RegisterDTO userDTO) {
            if (userDTO == null) return null;
            return User.builder()
                    .name(userDTO.getName())
                    .email(userDTO.getEmail())
                    .password(userDTO.getPassword())
                    .memberSince(new Date())
                    .role("Owner")
                    .address(userDTO.getAddress())
                    .pets(Optional.empty())
                    .breedHealthInfos(Optional.empty())
                    .build();
        }

    public static User convertFromUpdateUserDTOToUser(UpdateUserDTO updateUserDTO) {
        if (updateUserDTO == null) return null;
        return User.builder()
                .name(updateUserDTO.getName())
                .email(updateUserDTO.getEmail())
                .password(updateUserDTO.getNewPassword())
                .address(updateUserDTO.getAddress())
                .image(updateUserDTO.getImage())
                .build();
    }

    public static List<UserDTO> convertFromUsersToUserDTOs(List<User> users) {
        if (users == null) return new ArrayList<>();
        ArrayList<UserDTO> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(convertFromUserToUserDTO(user));
        }
        return dtos;
    }

    public static User convertFromUpdateUserDTOWithoutPasswordToUser(UpdateUserDTOWithoutPassword userDTO) {
        if (userDTO == null) return null;
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .address(userDTO.getAddress())
                .image(userDTO.getImage())
                .build();
    }

    public static User convertFromUpdateUserDTOWithPasswordToUser(UpdateUserDTOWithPassword userDTO) {
        if (userDTO == null) return null;
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getNewPassword())
                .address(userDTO.getAddress())
                .image(userDTO.getImage())
                .build();
    }
}
