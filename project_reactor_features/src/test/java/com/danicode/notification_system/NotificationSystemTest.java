package com.danicode.notification_system;

import com.danicode.notification_system.models.NotificationEvent;
import com.danicode.notification_system.service.EmailService;
import com.danicode.notification_system.service.NotificationService;
import com.danicode.notification_system.service.PhoneService;
import com.danicode.notification_system.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotificationSystemTest {
    private NotificationService mockTeamService;
    private NotificationService mockEmailService;
    private NotificationService mockPhoneService;

    private NotificationSystem notificationService;

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
    }
}
