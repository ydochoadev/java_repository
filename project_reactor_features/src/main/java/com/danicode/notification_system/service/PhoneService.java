package com.danicode.notification_system.service;

import com.danicode.notification_system.models.NotificationEvent;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

public class PhoneService implements NotificationService{
    @Override
    public Mono<Boolean> sendNotification(NotificationEvent event) {
        return Mono.fromCallable(() -> {
            Thread.sleep(1000);
            // simular error con 15% de probabilidad
            if (ThreadLocalRandom.current().nextInt(100) <= 20) {
                throw new RuntimeException("Error on send message in Phone call");
            }
            System.out.println("Message in Email OK: " + event.getMessage());

            return true;
        });
    }
}
