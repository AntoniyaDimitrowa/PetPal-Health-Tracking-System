package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificationRepositoryJPA extends JpaRepository<NotificationEntity, Long> {
}
