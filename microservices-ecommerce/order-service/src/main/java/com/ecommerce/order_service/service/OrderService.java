package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    // Se agrega userId para saber a quién pertenece el pedido para buscarlo
    CompletableFuture<OrderResponse> placeOrder(OrderRequest orderRequest, String userId); // Create

    // List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getOrders(String userId, boolean isAdmin);

    void deleteOrder(Long id);
}
