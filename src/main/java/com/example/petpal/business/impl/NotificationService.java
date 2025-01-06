package com.example.petpal.business.impl;

import com.example.petpal.business.domain.HealthNotification;
import com.example.petpal.business.exception.NotificationNotFoundException;
import com.example.petpal.controller.converters.NotificationConverter;
import com.example.petpal.controller.dto.NotificationDTO;
import com.example.petpal.persistence.INotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final INotificationRepository notificationRepository;

    public Page<NotificationDTO> getNotifications(String status, int page, int size) {
        // Define pagination settings
        Pageable pageable = PageRequest.of(page - 1, size);

        // Determine "read" status from the provided status string
        boolean isRead = "read".equalsIgnoreCase(status);

        // Retrieve notifications from the repository
        Page<HealthNotification> notifications = notificationRepository.findByIsRead(isRead, pageable);

        // Convert domain objects to DTOs and return
        return notifications.map(NotificationConverter::toDTO);
    }

    public void markAsRead(Long notificationId) {
        // Find the notification by its ID
        HealthNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));

        // Mark the notification as read
        notification.setRead(true);

        // Save the updated notification
        notificationRepository.updateNotification(notification);
    }
}
