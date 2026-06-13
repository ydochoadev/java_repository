package com.danicode;

import lombok.extern.java.Log;

@Log
public class Main {
    public static void main(String[] args) {
        // Inmutables
        final ReactiveStream<String> stringStream = new ReactiveStream<>(); // Publisher 1
        final ReactiveStream<Integer> integerStream = new ReactiveStream<>(); // Publisher 2

        final String subscriberName1 = "Subscriber 1";
        final String subscriberName2 = "Subscriber 2";
        final String subscriberName3 = "Subscriber 3";
        final String subscriberName4 = "Subscriber 4";

        // Primer subscriptor para el publisher de String
        final Subscriber<String> stringSubscriber1 = new SubscriberImpl<>(
                str -> "Longitud: " + str.length(),
                subscriberName1
        );
        // Segundo subscriptor para el publisher de String
        final Subscriber<String> stringSubscriber2 = new SubscriberImpl<>(
                String::toUpperCase,
                subscriberName2
        );
        // Primer subscriptor para el publisher de Integer
        final Subscriber<Integer> integerSubscriber1 = new SubscriberImpl<>(
                number -> "Value: " + number,
                subscriberName3
        );
        // Segundo subscriptor para el publisher de Integer
        final Subscriber<Integer> integerSubscriber2 = new SubscriberImpl<>(
                number -> "Potencia: " + (number * number),
                subscriberName4
        );

        // Relacionando publisher y subscriber
        stringStream
                .subscribe(stringSubscriber1) // Método subscribe retorna el mismo objeto (patrón chain)
                .subscribe(stringSubscriber2); // por ello se puede aplicar 2 veces el mismo método subscribe

        integerStream
                .subscribe(integerSubscriber1)
                .subscribe(integerSubscriber2);

        log.info("--- [Strings] ---");
        // Emitiendo datos
        stringStream.emit("hello world");
        stringStream.emit("this is a string");
        stringStream.emit("teclado, mouse y monitor");

        log.info("--- [Numbers] ---");
        integerStream.emit(5);
        integerStream.emit(10);
        integerStream.emit(15);
        integerStream.emit(12);

        // Desuscribir
        stringStream.unsubscribe(stringSubscriber1);
        integerStream.unsubscribe(integerSubscriber2);

    }
}
