package com.danicode;

import com.danicode.error_handler.HandleDisabledVideoGame;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Log
public class Main {
    public static void main(String[] args) {
        // Se publica un valor
       /* Mono<String> stringMono = Mono.just("Hello World xxx")
                .doOnNext(value -> log.info("[onNext]: " + value))
                .doOnSuccess(value -> log.info("[onSuccess]: " + value))
                .doOnError(err -> log.info("[onError]: " + err));
        // Necesita un subscriptor
        stringMono.subscribe(
                data -> log.info("Receiving data: " + data),
                err -> log.info("Error: " + err.getMessage()),
                () -> log.info("Complete Success")
        );

        // Publicador Flux
        // doOnNext => Se ejecuta varias veces
        // doOnComplete => Solo se ejecuta 1 vez
        Flux<String> stringFlux = Flux.just("Java", "Spring", "Reactor")
                .doOnNext(data -> log.info("[onNext]: " + data))
                .doOnComplete(() -> log.info("[OnComplete]: Success"));
        // Consumidor del Flux
        stringFlux.subscribe(
                data -> log.info("Received data: " + data),
                err -> log.info("Error: " + err.getMessage()),
                () -> log.info("Complete Success")
        );*/

        // Subscripciones de ejemplo
        /*PipelineTopSelling.getTopSellingVideoGames()
                .subscribe(System.out::println);

        PipelineSumAllPricesInDiscount.getSumAllPricesInDiscount()
                .subscribe(System.out::println);

        PipelineAllComments.getAllReviewsComments()
                .subscribe(System.out::println);*/

        /*log.info("****** FLAPMAP ******");
        // Combinación de 2 flujo con flapMap
        Flux<String> fluxA = Flux.just("1", "2", "3").delayElements(Duration.ofMillis(100));
        Flux<String> fluxB = Flux.just("A", "B").delayElements(Duration.ofMillis(50));
        // Tercer flux combinado
        Flux<String> combinedFlux = fluxA.flatMap(strA -> fluxB.map(strB -> strA + " - " + strB));*/
        // Subscribirse
        /*combinedFlux.subscribe(System.out::println);
        combinedFlux
                .map(String::toLowerCase)
                .doOnNext(System.out::println)
                .subscribe();*/

        // Merge
        /*log.info("****** CONCAT ******");
        Flux<String> combineFlux2 = Flux.concat(fluxA, fluxB);
        combineFlux2
                .doOnNext(System.out::println)
                .blockLast(); // Evita terminar la ejecución antes de procesar los flujos

        log.info("****** OPERADOR ZIP ******");
        // Envíos
        Flux<String> fluxShipments = Flux.just("Shipment 1", "Shipment 2", "Shipment 3").delayElements(Duration.ofMillis(120));
        // Almacén
        Flux<String> fluxWarehouse = Flux.just("Stock 1", "Stock 2", "Stock 3").delayElements(Duration.ofMillis(50));
        Flux<String> fluxPayments = Flux.just("Payment 1", "Payment 2", "Payment 3").delayElements(Duration.ofMillis(150));
        Flux<String> fluxConfirms = Flux.just("Confirm 1", "Confirm 2", "Confirm  3").delayElements(Duration.ofMillis(20));

        // Combina 2 flujos
        // Flux<String> reportFlux = Flux.zip(fluxShipments, fluxWarehouse, (shipment, stock) -> shipment + " - " + stock);
        // Combina 2 varios flujos
        // Flx.zip(tupla1, tupla2,tupla3, tupla4)
        Flux<String> allReportFlux = Flux.zip(fluxShipments, fluxWarehouse, fluxPayments, fluxConfirms)
                .map(tuple -> tuple.getT1() + " - " + tuple.getT2() + " - " + tuple.getT3() + " - " + tuple.getT4());
        allReportFlux.doOnNext(System.out::println).blockLast();*/

        log.info("****** HANDLER ******");
        HandleDisabledVideoGame.handleDisabledVideoGame().subscribe(System.out::println);

    }
}