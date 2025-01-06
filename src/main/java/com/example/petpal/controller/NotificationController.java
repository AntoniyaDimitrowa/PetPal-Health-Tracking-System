package com.example.petpal.controller;

import com.example.petpal.business.impl.NotificationService;
import com.example.petpal.controller.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationDTO>> getNotifications(
            @RequestParam String status,
            @RequestParam int page,
            @RequestParam int size) {
        Page<NotificationDTO> notifications = notificationService.getNotifications(status, page, size);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}
