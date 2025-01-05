package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.HealthAnalysisResult;
import com.example.petpal.business.domain.User;
import com.example.petpal.persistence.entity.NotificationEntity;

public class NotificationConverter {

    public static NotificationEntity analysisResultToNotificationEntity(HealthAnalysisResult result, User user) {
        return NotificationEntity.builder()
                .date(result.getDate())
                .pet(PetConverter.convertFromPetToPetEntity(result.getPet()))
                .user(UserConverter.convertFromUserToUserEntity(user))
                .message(result.getMessage())
                .isRead(result.isRead())
                .build();
    }
}
