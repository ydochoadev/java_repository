package com.danicode.notification_system;

import com.danicode.notification_system.models.NotificationEvent;
import com.danicode.notification_system.service.EmailService;
import com.danicode.notification_system.service.NotificationService;
import com.danicode.notification_system.service.PhoneService;
import com.danicode.notification_system.service.TeamService;
import lombok.Getter;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NotificationSystem {

    private final Sinks.Many<NotificationEvent> mainEvent; // Emitir a varios subscriptores

    @Getter
    private final Sinks.Many<NotificationEvent> historySink;

    private final NotificationService teamsService;
    private final NotificationService emailService;
    private final NotificationService phoneService;

    private final Sinks.One<NotificationEvent> teamsSink;
    private final Sinks.One<NotificationEvent> emailSink;
    private final Sinks.One<NotificationEvent> phoneSink;

    // cache
    private final ConcurrentMap<String, NotificationEvent> notificationCache;

    public NotificationSystem() {
        // onBackpressureBuffer: guarda los datos en un buffer cuando los subscriptores no pueden procesar tanta carga
        this.mainEvent = Sinks.many().multicast().onBackpressureBuffer();
        this.historySink = Sinks.many().replay().limit(50); // registro de las últimas 50 notificaciones
        this.teamsSink = Sinks.one();
        this.emailSink = Sinks.one();
        this.phoneSink = Sinks.one();

        this.teamsService = new TeamService();
        this.emailService = new EmailService();
        this.phoneService = new PhoneService();

        this.notificationCache = new ConcurrentHashMap<>();
    }
}
