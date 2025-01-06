package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.HealthAnalysisResult;
import com.example.petpal.business.domain.HealthNotification;
import com.example.petpal.business.domain.User;
import com.example.petpal.controller.HealthController;
import com.example.petpal.persistence.INotificationRepository;
import com.example.petpal.persistence.INotificationRepositoryJPA;
import com.example.petpal.persistence.converters.NotificationConverter;
import com.example.petpal.persistence.entity.NotificationEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    @Override
    public Page<HealthNotification> findByIsRead(boolean isRead, Pageable pageable) {
        var notificationEntities = notificationRepositoryJPA.findByIsRead(isRead, pageable);
        return notificationEntities.map(NotificationConverter::convertToDomain);
    }

    @Override
    public void updateNotification(HealthNotification notification) {
        // Convert domain object to entity and save using JPA repository
        NotificationEntity entity = NotificationConverter.domainToEntity(notification);
        notificationRepositoryJPA.save(entity); // JPA handles both insert and update
    }

    @Override
    public Optional<HealthNotification> findById(Long id) {
        // Fetch entity by ID and convert to domain object
        return notificationRepositoryJPA.findById(id)
                .map(NotificationConverter::convertToDomain);
    }
}
