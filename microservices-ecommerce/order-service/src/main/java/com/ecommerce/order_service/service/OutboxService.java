package com.ecommerce.order_service.service;

import com.ecommerce.order_service.event.OrderPlaceEvent;
import com.ecommerce.order_service.model.OutboxEvent;

import java.util.List;

public interface OutboxService {
    // Se busca solo los eventos que aún no han sido enviados exitosamente
    void saveOrderPlaceEvent(OrderPlaceEvent event, boolean isProcessed);
    // Lista de pendientes
    List<OutboxEvent> getPendingOutboxEvents();
    // Marcar los pendientes como procesados
    void markAsProcessed(Long id);
}
