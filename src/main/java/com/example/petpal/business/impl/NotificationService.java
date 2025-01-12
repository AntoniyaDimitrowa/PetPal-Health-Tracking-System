package com.example.petpal.business.impl;

import com.example.petpal.business.domain.HealthNotification;
import com.example.petpal.business.exception.NotificationNotFoundException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.configuration.security.token.IAccessToken;
import com.example.petpal.controller.converters.NotificationConverter;
import com.example.petpal.controller.dto.NotificationDTO;
import com.example.petpal.persistence.INotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final INotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final IAccessToken requestAccessToken;

    public Page<NotificationDTO> getNotifications(String status, int page, int size, Long userId) throws UnauthorizedDataAccessException {
        // Ensure the logged-in user is accessing their own data
        if (!Objects.equals(requestAccessToken.getUserId(), userId)) {
            throw new UnauthorizedDataAccessException();
        }

        Pageable pageable = PageRequest.of(page - 1, size);

        boolean isRead = "read".equalsIgnoreCase(status);

        // Fetch user-specific notifications with the given read status
        Page<HealthNotification> notifications = notificationRepository.findByIsReadAndUserId(isRead, userId, pageable);

        return notifications.map(NotificationConverter::toDTO);
    }

    public void markAsRead(Long notificationId) throws UnauthorizedDataAccessException {
        // Find and update the notification as read
        HealthNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));
        if (!Objects.equals(requestAccessToken.getUserId(), notification.getUser().getId())) {
            throw new UnauthorizedDataAccessException();
        }
        notification.setRead(true);
        notificationRepository.updateNotification(notification);
    }

    public void sendNotification(Long userId, int unreadCount) {
        // Send a WebSocket message to notify the user of unread notifications
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, unreadCount);
    }

    public int getUnreadCountForUser(Long userId) throws UnauthorizedDataAccessException {
        if (!Objects.equals(requestAccessToken.getUserId(), userId)) {
            throw new UnauthorizedDataAccessException();
        }

        return notificationRepository.getUnreadCountByUserId(userId);
    }
}
