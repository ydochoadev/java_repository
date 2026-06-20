package com.danicode;

import com.danicode.pipelines.PipelineAllComments;
import com.danicode.pipelines.PipelineSumAllPricesInDiscount;
import com.danicode.pipelines.PipelineTopSelling;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

        PipelineTopSelling.getTopSellingVideoGames()
                .subscribe(System.out::println);

        PipelineSumAllPricesInDiscount.getSumAllPricesInDiscount()
                .subscribe(System.out::println);

        PipelineAllComments.getAllReviewsComments()
                .subscribe(System.out::println);
    }
}