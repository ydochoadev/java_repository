package com.danicode.notification_system.service;

import com.danicode.notification_system.models.NotificationEvent;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

public class EmailService implements NotificationService {
    @Override
    public Mono<Boolean> sendNotification(NotificationEvent event) {
        return Mono.fromCallable(() -> {
            Thread.sleep(300);
            // simular error con 15% de probabilidad
            if (ThreadLocalRandom.current().nextInt(100) <= 15) {
                throw new RuntimeException("Error on send message in Email");
            }
            System.out.println("Message in Email OK: " + event.getMessage());

            return true;
        });
    }
}
