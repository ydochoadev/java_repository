package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.model.OrderStatus;

import java.util.List;

public interface OrderService {
    // Se agrega userId para saber a quién pertenece el pedido para buscarlo
    OrderResponse placeOrder(OrderRequest orderRequest, String userId); // Create

    // List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getOrders(String userId, boolean isAdmin);

    void deleteOrder(Long id);

    void updateOrderStatus(String orderNumber, OrderStatus status);
}
