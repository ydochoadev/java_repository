package com.ecommerce.notification_service.config;

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
    /* Bean para convertir la serialización java a Jackson
    para convertir los mensajes a json */
    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // Cola: Donde se almacenan los mensajes esperando ser leídos
    @Bean(name = "notificationQueueBean")
    public Queue notificationQueue() {
        // duration: SI RabbitMQ se reinicia, la cola sobrevive
        return new Queue("notification-queue", true);
    }

    // Distribuidor: Recibe el mensaje y decide a dónde lo almacena
    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange("order-events");
    }

    // Bean que une: Conecta el exchange con la cola:
    // Si llega un msj con la etiqueta 'order.placed', envía una copia a la cola inventory-queue
    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(notificationQueue).to(orderEventsExchange).with("order.confirmed");
    }
}
