package com.example.petpal.persistence;

import com.example.petpal.business.domain.HealthAnalysisResult;
import com.example.petpal.business.domain.HealthNotification;
import com.example.petpal.business.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface INotificationRepository {
    void saveNotification(HealthAnalysisResult result, User user);

    Optional<HealthNotification> findById(Long id);

    Page<HealthNotification> findByIsReadAndUserId(boolean isRead, Long userId, Pageable pageable);

    void updateNotification(HealthNotification notification);

    int getUnreadCountByUserId(Long userId);
}
