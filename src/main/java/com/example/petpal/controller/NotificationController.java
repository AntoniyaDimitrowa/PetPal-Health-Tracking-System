package com.example.petpal.controller;

import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.business.impl.NotificationService;
import com.example.petpal.configuration.security.token.IAccessToken;
import com.example.petpal.controller.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final IAccessToken requestAccessToken;

    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<NotificationDTO>> getNotifications(
            @PathVariable(value = "userId") final long userId,
            @RequestParam String status,
            @RequestParam int page,
            @RequestParam int size) {
        try {
            // Get user-specific notifications
            Page<NotificationDTO> notifications = notificationService.getNotifications(status, page, size, userId);
            return ResponseEntity.ok(notifications);
        } catch (UnauthorizedDataAccessException e) {
            // Return 403 Forbidden if access is unauthorized
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        // Mark notification as read
        try {
            notificationService.markAsRead(notificationId);

            Long userId = requestAccessToken.getUserId(); // Get user ID from the access token
            long unreadCount = notificationService.getUnreadCountForUser(userId);

            // Send updated unread count to WebSocket topic
            messagingTemplate.convertAndSend("/topic/unread-count/" + userId, unreadCount);
            return ResponseEntity.ok().build();
        } catch (UnauthorizedDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(@RequestParam Long userId) {
        try {
            long unreadCount = notificationService.getUnreadCountForUser(userId);
            return ResponseEntity.ok(unreadCount);
        } catch (UnauthorizedDataAccessException e) {
            // Return 403 Forbidden if access is unauthorized
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
