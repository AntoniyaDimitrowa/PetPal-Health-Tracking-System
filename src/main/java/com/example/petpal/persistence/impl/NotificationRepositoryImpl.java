package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.HealthAnalysisResult;
import com.example.petpal.business.domain.User;
import com.example.petpal.controller.HealthController;
import com.example.petpal.persistence.INotificationRepository;
import com.example.petpal.persistence.INotificationRepositoryJPA;
import com.example.petpal.persistence.converters.NotificationConverter;
import com.example.petpal.persistence.entity.NotificationEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class NotificationRepositoryImpl implements INotificationRepository {
    private final INotificationRepositoryJPA notificationRepositoryJPA;
    private static final Logger log = LoggerFactory.getLogger(NotificationRepositoryImpl.class);

    @Override
    public void saveNotification(HealthAnalysisResult result, User user) {
        NotificationEntity entity = NotificationConverter.analysisResultToNotificationEntity(result, user);
        log.info("NOTIFICATION_ENTITY: {}", entity);

        notificationRepositoryJPA.save(entity);
    }
}
