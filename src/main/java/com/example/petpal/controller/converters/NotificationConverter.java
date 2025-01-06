package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.HealthNotification;
import com.example.petpal.controller.dto.NotificationDTO;

public class NotificationConverter {

    public static NotificationDTO toDTO(HealthNotification domain) {
        return NotificationDTO.builder()
                .id(domain.getId())
                .date(domain.getDate())
                .message(domain.getMessage())
                .petName(domain.getPet().getName())
                .isRead(domain.isRead())
                .build();
    }
}
