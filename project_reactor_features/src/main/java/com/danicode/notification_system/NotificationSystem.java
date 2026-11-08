package com.danicode.notification_system;

import com.danicode.notification_system.models.NotificationEvent;
import com.danicode.notification_system.models.NotificationStatus;
import com.danicode.notification_system.models.Priority;
import com.danicode.notification_system.service.EmailService;
import com.danicode.notification_system.service.NotificationService;
import com.danicode.notification_system.service.PhoneService;
import com.danicode.notification_system.service.TeamService;
import lombok.Getter;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;
import java.util.UUID;
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
        // Emite datos al subscritor
        this.teamsSink = Sinks.one();
        this.emailSink = Sinks.one();
        this.phoneSink = Sinks.one();

        this.teamsService = new TeamService();
        this.emailService = new EmailService();
        this.phoneService = new PhoneService();

        this.notificationCache = new ConcurrentHashMap<>();
    }

    private void updateEventStatus(NotificationEvent event) {
        // Si status es nulo, es la primera vez que se envía
        if (Objects.isNull(event.getStatus())) {
            event.setId(UUID.randomUUID().toString());
            event.setId(UUID.randomUUID().toString());
        }
        this.notificationCache.put(event.getId(), event);
    }

    private void updateErrorStatus(NotificationEvent event, String channel, Throwable error) {
        System.out.println("Error to send notification by " + channel + ", for event: " + event.getId() + " , error: " + error);
        NotificationEvent cacheEvent = this.notificationCache.get(event.getId());
        if (Objects.nonNull(cacheEvent)) {
            cacheEvent.setStatus(NotificationStatus.FAILED);
            this.historySink.tryEmitNext(cacheEvent);
        }
    }

    private void updateSuccessStatus(NotificationEvent event, String channel) {
        System.out.println("Success to send notification by " + channel + ", for event: " + event.getId());
        NotificationEvent cacheEvent = this.notificationCache.get(event.getId());
        if (Objects.nonNull(cacheEvent)) {
            cacheEvent.setStatus(NotificationStatus.DELIVERED);
            this.historySink.tryEmitNext(cacheEvent);
        }
    }

    private void routeEventByPriority(NotificationEvent event) {
        this.teamsSink.tryEmitValue(event); // Siempre se envía por teams
        var priority = event.getPriority();
        if (priority == Priority.HIGH || priority == Priority.MEDIUM) {
            this.emailSink.tryEmitValue(event);
        }

        if (priority == Priority.HIGH) {
            this.phoneSink.tryEmitValue(event);
        }
    }

    private static final String TEAMS_CHANNEL = "Teams";
    private static final String EMAIL_CHANNEL = "Email";
    private static final String PHONE_CHANNEL = "Phone";

    // Subscriptor para teams
    private void setupTeamsProcessor() {
        this.teamsSink
                .asMono()
                // Se vuelve a subscribir
                .repeat()
                .flatMap(event -> this.teamsService.sendNotification(event)
                        // Sobre qué hilo se subscribe
                        .subscribeOn(Schedulers.boundedElastic())
                        .doOnSuccess(success -> this.updateSuccessStatus(event, TEAMS_CHANNEL))
                        .doOnError(error -> this.updateErrorStatus(event, TEAMS_CHANNEL, error))
                        .onErrorResume(error -> Mono.just(false)))
                .subscribe();
    }

    // Subscriptor para Email
    private void emailProcessor() {
        this.emailSink
                .asMono()
                // Se vuelve a subscribir
                .repeat()
                .flatMap(event -> this.emailService.sendNotification(event)
                        // Sobre qué hilo se subscribe
                        .subscribeOn(Schedulers.boundedElastic())
                        .doOnSuccess(success -> this.updateSuccessStatus(event, EMAIL_CHANNEL))
                        .doOnError(error -> this.updateErrorStatus(event, EMAIL_CHANNEL, error))
                        .onErrorResume(error -> Mono.just(false)))
                .subscribe();
    }

    // Subscriptor para Phone
    private void phoneProcessor() {
        this.phoneSink
                .asMono()
                // Se vuelve a subscribir
                .repeat()
                .flatMap(event -> this.phoneService.sendNotification(event)
                        // Sobre qué hilo se subscribe
                        .subscribeOn(Schedulers.boundedElastic())
                        .doOnSuccess(success -> this.updateSuccessStatus(event, PHONE_CHANNEL))
                        .doOnError(error -> this.updateErrorStatus(event, PHONE_CHANNEL, error))
                        .onErrorResume(error -> Mono.just(false)))
                .subscribe();
    }
}
