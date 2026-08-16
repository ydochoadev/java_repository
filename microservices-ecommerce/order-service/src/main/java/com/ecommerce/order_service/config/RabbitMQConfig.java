package com.ecommerce.order_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "order-events";

    // Bean para convertir la serialización java a Jackson para convertir los mensajes a json
    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // 1. Declara infraestructura: al correr app, bean contacta a RabbitMQ y asegura que exchange exista
    // 2. Desacoplar: order-service envía msj hasta el exchange (no a las colas)
    // 3. Flexibilidad: Enrutamiento por patrones. Permite que msj tengan temas asociados (routing key)
    // y loc consumidores se suscriben a los temas que les interesa
    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Cola para escuchar a inventory-service de las órdenes confirmadas
    @Bean
    public Queue orderConfirmedQueue() {
        return new Queue("order-confirmed-queue", true);
    }

    // Bean que une: Conecta el exchange con la cola:
    // Si llega un msj con la etiqueta 'order.confirmed', envía una copia a la cola order-confirmed-queue
    @Bean
    public Binding confirmedBinding(Queue orderConfirmedQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(orderConfirmedQueue).to(orderEventsExchange).with("order.confirmed");
    }

    // Cola para escuchar a inventory-service de las órdenes canceladas
    @Bean
    public Queue orderCancelledQueue() {
        return new Queue("order-cancelled-queue", true);
    }

    // Bean que une: Conecta el exchange con la cola:
    // Si llega un msj con la etiqueta 'order.confirmed', envía una copia a la cola order-cancelled-queue
    @Bean
    public Binding cancelledBinding(Queue orderCancelledQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(orderCancelledQueue).to(orderEventsExchange).with("order.cancelled");
    }
}
