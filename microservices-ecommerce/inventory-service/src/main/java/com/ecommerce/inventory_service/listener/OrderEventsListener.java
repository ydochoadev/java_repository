package com.ecommerce.inventory_service.listener;

import com.ecommerce.inventory_service.event.OrderCancelledEvent;
import com.ecommerce.inventory_service.event.OrderConfirmedEvent;
import com.ecommerce.inventory_service.event.OrderPlaceEvent;
import com.ecommerce.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderEventsListener {

    private final InventoryService inventoryService;
    private final RabbitTemplate rabbitTemplate;

    // @RabbitListener: Hace que springboot arranque un proceso en 2° plano (hilo) con una conexión a RabbiTMQ
    @RabbitListener(queues = "inventory-queue")
    public void handleOrderPlaceEvent(OrderPlaceEvent event) {
        log.info("Evento recibido en Inventario para la orden: {}", event.orderNumber());
        try {
            boolean allProductsInStock = event.items().stream()
                    .allMatch(item -> inventoryService.isInStock(item.sku(), item.quantity()));
            if (!allProductsInStock) {
                cancelOrder(event, "Stock insuficiente en uno o más productos");
                return;
            }
            // Si hay stock, reducirlo
            event.items().forEach(item -> {
                inventoryService.reduceStock(item.sku(), item.quantity());
            });

            // Confirmar
            OrderConfirmedEvent confirmedEvent = new OrderConfirmedEvent(
                    event.orderNumber(), event.email()
            );
            rabbitTemplate.convertAndSend("order-events", "order.confirmed", confirmedEvent);

            log.info("Stock descontado para SKU {}", event.orderNumber());
        } catch (Exception e) {
            log.error("Error al procesar el stock: {}", e.getMessage());
            cancelOrder(event, "Error técnico en el procesamiento de inventario");
        }
    }

    private void cancelOrder(OrderPlaceEvent event, String reason) {
        OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(event.orderNumber(),
                event.orderNumber(),
                reason);
        // Cuando no hay stock suficiente, se dispara otro evento
        rabbitTemplate.convertAndSend("order-events", "order.cancelled", orderCancelledEvent);
    }
}
