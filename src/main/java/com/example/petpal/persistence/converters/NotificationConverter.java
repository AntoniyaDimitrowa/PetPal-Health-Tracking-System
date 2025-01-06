package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.HealthNotification;
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

    public static HealthNotification convertToDomain(NotificationEntity entity) {
        return HealthNotification.builder()
                .id(entity.getResultId())
                .date(entity.getDate())
                .message(entity.getMessage())
                .pet(PetConverter.convertFromPetEntityToPet(entity.getPet()))
                .user(UserConverter.convertFromUserEntityToUser(entity.getUser()))
                .isRead(entity.isRead())
                .build();
    }

    public static NotificationEntity domainToEntity(HealthNotification notification) {
        return NotificationEntity.builder()
                .resultId(notification.getId())
                .date(notification.getDate())
                .message(notification.getMessage())
                .pet(PetConverter.convertFromPetToPetEntity(notification.getPet()))
                .user(UserConverter.convertFromUserToUserEntity(notification.getUser()))
                .isRead(notification.isRead())
                .build();
    }
}
