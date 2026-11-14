package com.danicode.notification_system;

import com.danicode.notification_system.models.NotificationEvent;
import com.danicode.notification_system.models.NotificationStatus;
import com.danicode.notification_system.models.Priority;
import com.danicode.notification_system.service.EmailService;
import com.danicode.notification_system.service.NotificationService;
import com.danicode.notification_system.service.PhoneService;
import com.danicode.notification_system.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationSystemTest {
    private NotificationService mockTeamService;
    private NotificationService mockEmailService;
    private NotificationService mockPhoneService;

    private NotificationSystem notificationSystem;

    // Tipo de dato especial para reactividad
    private AtomicInteger teamCallCount;
    private AtomicInteger emailCallCount;
    private AtomicInteger phoneCallCount;

    @BeforeEach
    void setup() {
        this.teamCallCount = new AtomicInteger(0);
        this.emailCallCount = new AtomicInteger(0);
        this.phoneCallCount = new AtomicInteger(0);

        this.mockTeamService = mock(TeamService.class);
        this.mockEmailService = mock(EmailService.class);
        this.mockPhoneService = mock(PhoneService.class);

        when(this.mockTeamService.sendNotification(any(NotificationEvent.class)))
                .thenAnswer(invocation -> {
                    this.teamCallCount.incrementAndGet();
                    return Mono.just(true);
                });

        when(this.mockEmailService.sendNotification(any(NotificationEvent.class)))
                .thenAnswer(invocation -> {
                    this.emailCallCount.incrementAndGet();
                    return Mono.just(true);
                });
        when(this.mockPhoneService.sendNotification(any(NotificationEvent.class)))
                .thenAnswer(invocation -> {
                    this.phoneCallCount.incrementAndGet();
                    return Mono.just(true);
                });

        this.notificationSystem = new NotificationSystem(
                this.mockTeamService,
                this.mockEmailService,
                this.mockPhoneService);
    }

    @Test
    @DisplayName("Should sends events with LOW priority")
    void testLowPriority() {
        NotificationEvent notificationEvent = this.createTestEvent(Priority.LOW);
        this.notificationSystem.publishEvent(notificationEvent);

        this.sleep(500);

        verify(mockTeamService, times(1)).sendNotification(notificationEvent);
        verify(mockEmailService, never()).sendNotification(notificationEvent);
        verify(mockPhoneService, never()).sendNotification(notificationEvent);

        assert this.teamCallCount.get() == 1;
        assert this.emailCallCount.get() == 0;
        assert this.phoneCallCount.get() == 0;
    }

    private NotificationEvent createTestEvent(Priority priority) {
        return NotificationEvent.builder()
                .id(UUID.randomUUID().toString())
                .source("TEST")
                .message("Test msg with priority: " + priority.toString())
                .priority(priority)
                .timestamp(LocalDateTime.now())
                .status(NotificationStatus.PENDING)
                .build();
    }

    private void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
