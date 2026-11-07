package com.danicode.notification_system.service;

import com.danicode.notification_system.models.NotificationEvent;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

public class TeamService implements NotificationService {

    @Override
    public Mono<Boolean> sendNotification(NotificationEvent event) {
        return Mono.fromCallable(() -> {
            Thread.sleep(150);
            // simular error con 10% de probabilidad
            if (ThreadLocalRandom.current().nextInt(10) == 0) {
                throw new RuntimeException("Error on send message in Teams");
            }
            System.out.println("Message in teams OK: " + event.getMessage());

            return true;
        });
    }
}
