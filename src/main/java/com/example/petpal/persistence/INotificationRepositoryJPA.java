package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface INotificationRepositoryJPA extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findByIsReadAndUserId(boolean isRead, Long userId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.user.id = :userId AND n.isRead = false")
    int getUnreadCountByUserId(@Param("userId") Long userId);}
