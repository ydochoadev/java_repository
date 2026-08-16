package com.ecommerce.notification_service.listener;

import com.ecommerce.notification_service.event.OrderCancelledEvent;
import com.ecommerce.notification_service.event.OrderConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventsListener {

    private final JavaMailSender mailSender;

    // @RabbitListener: Hace que springboot arranque un proceso en 2° plano (hilo) con una conexión a RabbiTMQ
    @RabbitListener(queues = "notification-queue")
    public void handleOrderConfirmedEvent(OrderConfirmedEvent event) {
        log.info("Pedido confirmado para la orden: {}", event.orderNumber());
        log.info("Enviando correo de confirmación a : {}", event.email());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("pedidos@ecommerce.com");
        message.setTo(event.email());
        message.setSubject("Orden Confirmada - " + event.orderNumber());
        message.setText(" Hola! \n\n" +
                "Tu pedido con número " + event.orderNumber() + " ha sidorecibido exitosamente. \n" +
                "Gracias por comprar con nosortros");
        mailSender.send(message);

        log.info("Correo enviando exitosamente para la orden {}", event.orderNumber());
    }

    @RabbitListener(queues = "notification-queue")
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Pedido confirmado para la orden: {}", event.orderNumber());

        log.info("Enviando correo de cancelación de la orden {} a {}", event.orderNumber(), event.email());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("pedidos@ecommerce.com");
        message.setTo(event.email());
        message.setSubject("Actualización de tu pedido - " + event.orderNumber());
        message.setText(" Hola! \n\n" +
                "Lamentamos informarte que tu pedido ha sido cancelado. \n\n" + "Motivo: " + event.reason() + "\n" +
                "Si se realizó algún cargo, será reembolsado a la brevedad.");
        mailSender.send(message);

        log.info("Correo enviando exitosamente para la orden {}", event.orderNumber());
    }
}
