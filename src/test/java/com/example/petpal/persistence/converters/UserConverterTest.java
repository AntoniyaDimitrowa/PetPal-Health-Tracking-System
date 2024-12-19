package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.User;
import com.example.petpal.persistence.entity.UserEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class UserConverterTest {

    private static final String USER_NAME = "John Doe";
    private static final String EMAIL = "john.doe@example.com";
    private static final String PASSWORD = "password123";
    private static final String ROLE = "Owner";
    private static final String ADDRESS = "1234 Main St";
    private static final String IMAGE = "image_url";
    private static final Date MEMBER_SINCE = new Date();

    private static final User user = User.builder()
            .id(1L)
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

    private static final UserEntity userEntity = UserEntity.builder()
            .id(1L)
            .name(USER_NAME)
            .email(EMAIL)
            .password(PASSWORD)
            .memberSince(MEMBER_SINCE)
            .role(ROLE)
            .address(ADDRESS)
            .image(IMAGE)
            .pets(new ArrayList<>())
            .breedHealthInfos(new ArrayList<>())
            .build();

    @Test
    void convertFromUserToUserEntity_shouldConvertUserToUserEntity() {
        UserEntity result = UserConverter.convertFromUserToUserEntity(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getMemberSince(), result.getMemberSince());
        assertEquals(user.getRole(), result.getRole());
        assertEquals(user.getAddress(), result.getAddress());
        assertEquals(user.getImage(), result.getImage());
    }

    @Test
    void convertFromUserToUserEntity_shouldReturnNullIfUserIsNull() {
        assertNull(UserConverter.convertFromUserToUserEntity(null));
    }

    @Test
    void convertFromUserEntityToUser_shouldConvertUserEntityToUser() {
        User result = UserConverter.convertFromUserEntityToUser(userEntity);

        assertNotNull(result);
        assertEquals(userEntity.getId(), result.getId());
        assertEquals(userEntity.getName(), result.getName());
        assertEquals(userEntity.getEmail(), result.getEmail());
        assertEquals(userEntity.getPassword(), result.getPassword());
        assertEquals(userEntity.getMemberSince(), result.getMemberSince());
        assertEquals(userEntity.getRole(), result.getRole());
        assertEquals(userEntity.getAddress(), result.getAddress());
        assertEquals(userEntity.getImage(), result.getImage());
    }

    @Test
    void convertFromUserEntityToUser_shouldReturnNullIfUserEntityIsNull() {
        assertNull(UserConverter.convertFromUserEntityToUser(null));
    }
}
