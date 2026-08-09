package com.ecommerce.notification_service.listener;

import com.ecommerce.notification_service.event.OrderPlaceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventsListener {

    // @RabbitListener: Hace que springboot arranque un proceso en 2° plano (hilo) con una conexión a RabbiTMQ
    @RabbitListener(queues = "notification-queue")
    public void handleOrderPlaceEvent(OrderPlaceEvent event) {
        log.info("Evento recibido en Inventario para la orden: {}", event.orderNumber());
        event.items().forEach(item -> {
            try {
                log.info("Enviando correo de confirmacióna : {}", event.email());

                log.info("Correo enviando exitosamente para la orden {}", event.orderNumber());
            } catch (Exception e) {
                log.error("Error al enviar correo: {}", e.getMessage());
            }
        });
    }
}
