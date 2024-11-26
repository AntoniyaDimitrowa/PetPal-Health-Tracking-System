package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.User;
import com.example.petpal.controller.dto.RegisterDTO;
import com.example.petpal.controller.dto.user.UpdateUserDTO;
import com.example.petpal.controller.dto.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    private static final String USER_NAME = "John Doe";
    private static final String EMAIL = "john.doe@example.com";
    private static final String PASSWORD = "password123";
    private static final String ROLE = "Owner";
    private static final String ADDRESS = "1234 Main St";
    private static final String IMAGE = "image_url";
    private static final Date MEMBER_SINCE = new Date();

    private static final User user = User.builder()
            .name(USER_NAME)
            .email(EMAIL)
            .password(PASSWORD)
            .memberSince(MEMBER_SINCE)
            .role(ROLE)
            .address(ADDRESS)
            .image(IMAGE)
            .pets(Optional.empty())
            .breedHealthInfos(Optional.empty())
            .build();
    private static final UserDTO userDTO = UserDTO.builder()
            .name(USER_NAME)
            .email(EMAIL)
            .password(PASSWORD)
            .memberSince(MEMBER_SINCE)
            .role(ROLE)
            .address(ADDRESS)
            .image(IMAGE)
            .pets(Optional.empty())
            .breedHealthInfos(Optional.empty())
            .build();
    private static final RegisterDTO registerDTO = RegisterDTO.builder()
            .name(USER_NAME)
            .email(EMAIL)
            .password(PASSWORD)
            .address(ADDRESS)
            .build();
    private static final UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
            .name(USER_NAME)
            .email(EMAIL)
            .newPassword(PASSWORD)
            .address(ADDRESS)
            .image(IMAGE)
            .build();

    @Test
    void convertFromUserToUserDTO_shouldConvertUserToUserDTO() {
        UserDTO result = UserConverter.convertFromUserToUserDTO(user);

        assertNotNull(result);
        assertEquals(USER_NAME, result.getName());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(PASSWORD, result.getPassword());
        assertEquals(MEMBER_SINCE, result.getMemberSince());
        assertEquals(ROLE, result.getRole());
        assertEquals(ADDRESS, result.getAddress());
        assertEquals(IMAGE, result.getImage());
    }

    @Test
    void convertFromUserToUserDTO_shouldReturnNullIfUserIsNull() {
        assertNull(UserConverter.convertFromUserToUserDTO(null));
    }

    @Test
    void convertFromUserDTOToUser_shouldConvertUserDTOToUser() {
        User result = UserConverter.convertFromUserDTOToUser(userDTO);

        assertNotNull(result);
        assertEquals(USER_NAME, result.getName());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(PASSWORD, result.getPassword());
        assertEquals(MEMBER_SINCE, result.getMemberSince());
        assertEquals(ROLE, result.getRole());
        assertEquals(ADDRESS, result.getAddress());
        assertEquals(IMAGE, result.getImage());
    }

    @Test
    void convertFromUserDTOToUser_shouldReturnNullIfUserDTOIsNull() {
        assertNull(UserConverter.convertFromUserDTOToUser(null));
    }

    @Test
    void convertFromRegisterDTOToUser_shouldConvertRegisterDTOToUser() {
        User result = UserConverter.convertFromRegisterDTOToUser(registerDTO);

        assertNotNull(result);
        assertEquals(USER_NAME, result.getName());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(PASSWORD, result.getPassword());
        assertEquals(ADDRESS, result.getAddress());
        assertNotNull(result.getMemberSince());
        assertEquals("Owner", result.getRole());
    }

    @Test
    void convertFromRegisterDTOToUser_shouldReturnNullIfRegisterDTOIsNull() {
        assertNull(UserConverter.convertFromRegisterDTOToUser(null));
    }

    @Test
    void convertFromUpdateUserDTOToUser_shouldConvertUpdateUserDTOToUser() {
        User result = UserConverter.convertFromUpdateUserDTOToUser(updateUserDTO);

        assertNotNull(result);
        assertEquals(USER_NAME, result.getName());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(PASSWORD, result.getPassword());
        assertEquals(ADDRESS, result.getAddress());
        assertEquals(IMAGE, result.getImage());
    }

    @Test
    void convertFromUpdateUserDTOToUser_shouldReturnNullIfUpdateUserDTOIsNull() {
        assertNull(UserConverter.convertFromUpdateUserDTOToUser(null));
    }

    @Test
    void convertFromUsersToUserDTOs_shouldConvertUserListToUserDTOList() {
        List<User> users = List.of(user);
        List<UserDTO> result = UserConverter.convertFromUsersToUserDTOs(users);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(USER_NAME, result.get(0).getName());
        assertEquals(EMAIL, result.get(0).getEmail());
        assertEquals(PASSWORD, result.get(0).getPassword());
    }

    @Test
    void convertFromUsersToUserDTOs_shouldReturnEmptyListIfUserListIsNull() {
        List<UserDTO> result = UserConverter.convertFromUsersToUserDTOs(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
