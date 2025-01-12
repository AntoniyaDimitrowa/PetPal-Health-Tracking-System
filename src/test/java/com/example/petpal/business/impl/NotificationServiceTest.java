package com.example.petpal.business.impl;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.HealthNotification;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.exception.NotificationNotFoundException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.configuration.security.token.IAccessToken;
import com.example.petpal.controller.converters.NotificationConverter;
import com.example.petpal.controller.dto.NotificationDTO;
import com.example.petpal.persistence.INotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class NotificationServiceTest {

    @Mock
    private INotificationRepository notificationRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private IAccessToken requestAccessToken;

    @InjectMocks
    private NotificationService notificationService;

    private static final Long USER_ID = 1L;
    private static final Long NOTIFICATION_ID = 1L;

    private static final Breed breed = new Breed(1L, "Labrador", "Labradors are friendly and outgoing.", null, 1.5, new ArrayList<>(Arrays.asList("Hip dysplasia")));

    private static final Pet pet = new Pet(1L, "Buddy", breed, Gender.MALE, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());

    private static final HealthNotification notification = HealthNotification.builder()
            .id(NOTIFICATION_ID)
            .isRead(false)
            .message("Test notification")
            .pet(pet)
            .date(new Date())
            .user(User.builder().id(USER_ID).build())
            .build();

    private static final NotificationDTO notificationDTO = NotificationDTO.builder()
            .id(NOTIFICATION_ID)
            .isRead(false)
            .message("Test notification")
            .petName(pet.getName())
            .date(new Date())
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getNotifications_shouldReturnNotificationsWhenUserIsAuthorized() throws UnauthorizedDataAccessException {
        String status = "unread";
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);

        // Mocked page of notifications containing the static `notification` object
        Page<HealthNotification> notificationsPage = new PageImpl<>(List.of(notification), pageable, 1);

        // Mocked page of DTOs (mapping result)
        Page<NotificationDTO> dtoPage = new PageImpl<>(List.of(notificationDTO), pageable, 1);

        when(requestAccessToken.getUserId()).thenReturn(USER_ID);
        when(notificationRepository.findByIsReadAndUserId(false, USER_ID, pageable)).thenReturn(notificationsPage);

        // Call the service method
        Page<NotificationDTO> result = notificationService.getNotifications(status, page, size, USER_ID);

        // Assertions
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.getTotalElements(), "The total elements in the result should match the mocked notifications");

        // Verifications
        verify(notificationRepository, times(1)).findByIsReadAndUserId(false, USER_ID, pageable);
    }



    @Test
    void getNotifications_shouldThrowUnauthorizedDataAccessExceptionWhenUserIsUnauthorized() {
        String status = "unread";
        int page = 1;
        int size = 10;
        when(requestAccessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () ->
                notificationService.getNotifications(status, page, size, USER_ID));

        verify(notificationRepository, never()).findByIsReadAndUserId(anyBoolean(), anyLong(), any(Pageable.class));
    }

    @Test
    void markAsRead_shouldUpdateNotificationAsReadWhenUserIsAuthorized() throws UnauthorizedDataAccessException {
        when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.of(notification));
        when(requestAccessToken.getUserId()).thenReturn(USER_ID);

        notificationService.markAsRead(NOTIFICATION_ID);

        assertTrue(notification.isRead());
        verify(notificationRepository, times(1)).findById(NOTIFICATION_ID);
        verify(notificationRepository, times(1)).updateNotification(notification);
    }

    @Test
    void markAsRead_shouldThrowUnauthorizedDataAccessExceptionForDifferentUser() {
        when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.of(notification));
        when(requestAccessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () ->
                notificationService.markAsRead(NOTIFICATION_ID));

        verify(notificationRepository, times(1)).findById(NOTIFICATION_ID);
        verify(notificationRepository, never()).updateNotification(any());
    }

    @Test
    void markAsRead_shouldThrowNotificationNotFoundExceptionForInvalidId() {
        when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.empty());

        assertThrows(NotificationNotFoundException.class, () ->
                notificationService.markAsRead(NOTIFICATION_ID));

        verify(notificationRepository, times(1)).findById(NOTIFICATION_ID);
    }

    @Test
    void sendNotification_shouldSendCorrectMessage() {
        int unreadCount = 5;

        notificationService.sendNotification(USER_ID, unreadCount);

        verify(messagingTemplate, times(1))
                .convertAndSend("/topic/notifications/" + USER_ID, unreadCount);
    }

    @Test
    void getUnreadCountForUser_shouldReturnCorrectUnreadCount() throws UnauthorizedDataAccessException {
        int unreadCount = 5;
        when(requestAccessToken.getUserId()).thenReturn(USER_ID);
        when(notificationRepository.getUnreadCountByUserId(USER_ID)).thenReturn(unreadCount);

        int result = notificationService.getUnreadCountForUser(USER_ID);

        assertEquals(unreadCount, result);
        verify(notificationRepository, times(1)).getUnreadCountByUserId(USER_ID);
    }

    @Test
    void getUnreadCountForUser_shouldThrowUnauthorizedDataAccessExceptionForDifferentUser() {
        when(requestAccessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () ->
                notificationService.getUnreadCountForUser(USER_ID));

        verify(notificationRepository, never()).getUnreadCountByUserId(anyLong());
    }
}
