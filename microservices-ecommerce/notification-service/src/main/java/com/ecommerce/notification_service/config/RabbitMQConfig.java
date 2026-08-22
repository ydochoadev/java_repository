package com.ecommerce.notification_service.config;

import org.springframework.amqp.core.*;
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
        return QueueBuilder.durable("notification-queue")
                .withArgument("x-dead-letter-exchange", "notification-dlx")
                .withArgument("x-dead-letter-routing-key", "notification.dead")
                .build();
    }

    // Distribuidor: Recibe el mensaje y decide a dónde lo almacena
    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange("order-events");
    }

    // Para las notificaciones fallidas: DirectExchange (canal exclusivo)
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("notification-dlx");
    }

    // Cola para las notificaciones fallidas
    @Bean(name = "notificationQueueBean")
    public Queue deadLetterQueue() {
        return new Queue("notification-dlq", true);
    }

    // Bean que une: Conecta el exchange con la cola:
    // Si llega un msj con la etiqueta 'order.confirmed', envía una copia a la cola notification-queue
    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(notificationQueue).to(orderEventsExchange).with("order.confirmed");
    }

    @Bean
    public Binding cancelledBinding(Queue notificationQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(notificationQueue).to(orderEventsExchange).with("order.cancelled");
    }

    // Binding para las notificaciones fallidas
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with("notification.dead");
    }
}
