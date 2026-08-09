package com.ecommerce.inventory_service.listener;

import com.ecommerce.inventory_service.event.OrderPlaceEvent;
import com.ecommerce.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderEventsListener {

    private final InventoryService inventoryService;

    // @RabbitListener: Hace que springboot arranque un proceso en 2° plano (hilo) con una conexión a RabbiTMQ
    @RabbitListener(queues = "inventory-queue")
    public void handleOrderPlaceEvent(OrderPlaceEvent event) {
        log.info("Evento recibido en Inventario para la orden: {}", event.orderNumber());
        event.items().forEach(item -> {
            try {
                inventoryService.reduceStock(item.sku(), item.quantity());
                log.info("Stock descontado para SKU {} - Cantidad {}", item.sku(), item.quantity());
            } catch (Exception e) {
                log.error("Error al procesar el stock para SKU {}: {}", item.sku(), e.getMessage());
            }
        });
    }
}
