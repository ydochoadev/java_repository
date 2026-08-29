package com.ecommerce.order_service.service;

import com.ecommerce.order_service.event.OrderPlaceEvent;

import java.util.List;

public interface OutboxService {
    // Se busca solo los eventos que aún no han sido enviados exitosamente
    void saveOrderPlaceEvent(OrderPlaceEvent event, boolean isProcessed);
}
