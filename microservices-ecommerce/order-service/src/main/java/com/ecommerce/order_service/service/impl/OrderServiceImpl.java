package com.ecommerce.order_service.service.impl;

import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.exception.ResourceNotFoundException;
import com.ecommerce.order_service.mapper.OrderMapper;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.service.OrderService;
import com.ecommerce.order_service.service.client.InventoryClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    // private final WebClient.Builder webClientBuilder;
    private final InventoryClient inventoryClient;

    @Value("${order.enabled:true}")
    private boolean ordersEnabled;

    public OrderResponse fallbackMethod(OrderRequest orderRequest, String userId, Throwable throwable) {
        log.error("Circuit Breaker activado. Causa: {}", throwable.getMessage());

        // return new OrderResponse(0L,  "00000", Collections.emptyList());
        throw new RuntimeException("El servicio de Inventario no responde. Intentar más tarde");
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    public OrderResponse placeOrder(OrderRequest orderRequest, String userId) {
        if (!ordersEnabled) {
            log.warn("Pedido rechazado: Servicio deshabilitado por configuración");
            throw new RuntimeException("El servicio de pedidos está en mantenimiento");
        }

        log.info("Colocando nueva orden...");
        Order order = orderMapper.toOrder(orderRequest);
        order.setUserId(userId);

        for (var item : order.getOrderLineItemsList()) {
            String sku = item.getSku();
            Integer quantity = item.getQuantity();
            // Realizar la llamada a inventory
            try {
                /*webClientBuilder.build().put()
                        .uri("http://localhost:8082/api/v1/inventory/reduce/" + sku,
                                uriBuilder -> uriBuilder.queryParam("quantity", quantity).build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block(); // petición bloqueante*/
                inventoryClient.reduceStock(sku, quantity);
            } catch (Exception e) {
                log.error("Error al reducir stock del producto {}. {}", sku, e.getMessage());
                throw new IllegalArgumentException("No se pudo procesa la orden: Stock insuficiente o error de inventario");
            }
        }

        order.setOrderNumber(UUID.randomUUID().toString());
        // order.setOrderLineItemsList(orderLineItems);

        // Guardamos y capturamos la entidad persistida
        Order savedOrder = orderRepository.save(order);
        log.info("Orden guardada con éxito. ID: {}", savedOrder.getId());

        return orderMapper.toOrderResponse(savedOrder);
    }

    /*@Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }*/

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden", "id", id));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(String userId, boolean isAdmin) {
        List<Order> orders;
        if (isAdmin) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByUserId(userId);
        }
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Orden", "id", id);
        }
        orderRepository.deleteById(id);
        log.info("Orden eliminada. ID: {}", id);
    }
}
