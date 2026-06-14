package com.danicode;

import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@Log
public class Main {
    public static void main(String[] args) {
        // Se publica un valor
        Mono<String> stringMono = Mono.just("Hello World xxx")
                .doOnNext(value -> log.info("[onNext]: " + value));
        // Necesita un subscriptor
        stringMono.subscribe();
    }
}