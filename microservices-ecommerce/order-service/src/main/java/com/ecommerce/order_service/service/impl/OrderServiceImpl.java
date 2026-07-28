package com.ecommerce.order_service.service.impl;

import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.event.OrderPlaceEvent;
import com.ecommerce.order_service.exception.ResourceNotFoundException;
import com.ecommerce.order_service.mapper.OrderMapper;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    // private final InventoryClient inventoryClient; ya no se usará inventory por http
    private final RabbitTemplate rabbitTemplate;

    @Value("${order.enabled:true}")
    private boolean ordersEnabled;

    public OrderResponse fallbackMethod(OrderRequest orderRequest, String userId, Throwable throwable) {
        log.error("Circuit Breaker activado. Causa: {}", throwable.getMessage());

        throw new RuntimeException("El servicio de Inventario no responde. Intentar más tarde");

    }

    @Override
    @Transactional
    /* @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory") */
    public OrderResponse placeOrder(OrderRequest orderRequest, String userId) {
        if (!ordersEnabled) {
            log.warn("Pedido rechazado: Servicio deshabilitado por configuración");
            throw new RuntimeException("El servicio de pedidos está en mantenimiento");
        }

        log.info("Colocando nueva orden...");
        Order order = orderMapper.toOrder(orderRequest);
        order.setUserId(userId);

        /*for (var item : order.getOrderLineItemsList()) {
            String sku = item.getSku();
            Integer quantity = item.getQuantity();
            // Realizar la llamada a inventory
            try {
                inventoryClient.reduceStock(sku, quantity);
            } catch (Exception e) {
                log.error("Error al reducir stock del producto {}. {}", sku, e.getMessage());
                throw new IllegalArgumentException("No se pudo procesa la orden: Stock insuficiente o error de inventario");
            }
        }*/

        order.setOrderNumber(UUID.randomUUID().toString());
        // Guardamos y capturamos la entidad persistida
        Order savedOrder = orderRepository.save(order);
        log.info("Orden guardada con éxito. ID: {}", savedOrder.getId());
        // Contrato
        List<OrderPlaceEvent.OrderItemEvent> orderItems = order.getOrderLineItemsList()
                .stream()
                .map(item -> new OrderPlaceEvent.OrderItemEvent(
                        item.getSku(), item.getPrice().toString(), item.getQuantity()
                )).toList();
        OrderPlaceEvent event = new OrderPlaceEvent(savedOrder.getOrderNumber(), orderRequest.getEmail(), orderItems);
        rabbitTemplate.convertAndSend("order-events", "order.placed", event);
        log.info("Evento enviado a RabbitMQ para la orden: {}", savedOrder.getOrderNumber());

        return orderMapper.toOrderResponse(savedOrder);
    }

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
