package com.ecommerce.order_service.scheduler;

import com.ecommerce.order_service.event.OrderPlaceEvent;
import com.ecommerce.order_service.model.OutboxEvent;
import com.ecommerce.order_service.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageRelayer {

    private final RabbitTemplate rabbitTemplate;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    // Ejecutar cada 10 segundos: busca a los OutboxEvent pendientes
    @Scheduled(fixedRate = 10000)
    public void relayMessage(String message) {
        List<OutboxEvent> pendingEvents = outboxService.getPendingOutboxEvents();
        if (!pendingEvents.isEmpty()) {
            log.info("Relayer: Detectados {} mensajes pendientes", pendingEvents.size());
            for (OutboxEvent event : pendingEvents) {
                try {
                    OrderPlaceEvent originalEvent = objectMapper.readValue(event.getPayload(), OrderPlaceEvent.class);
                    rabbitTemplate.convertAndSend("order-events", "order.placed", originalEvent);
                    outboxService.markAsProcessed(event.getId());
                } catch (JacksonException e) {
                    log.error("Error al deserializar evento {}: {}", event.getId(), e.getMessage());
                } catch (AmqpException e) {
                    log.error("Falló el reintento para {}: {}", event.getAggregateId(), e.getMessage());
                }
            }
        }
    }
}
