package com.danicode.callbacks;

import com.danicode.database.Database;
import com.danicode.models.Videogame;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class CallBacksExample {
    public static Flux<Videogame> callbacks() {
        // Todos los callbacks empiezan con "doOn"
        return Database.getDataAsFlux()
                // Se ejecuta cuando un Subscriber se suscribe al flujo. Útil para logs o inicialización
                .doOnSubscribe(subs -> System.out.println("[DoOnSubscribe]: {}"))
                // Se llama cuando el Subscriber solicita elementos (request(n), backpressure)
                .doOnRequest(n -> System.out.println("[DoOnRequest]: " + n))
                // Emite el siguiente elemento
                .doOnNext(videogame -> System.out.println("[DoOnNext]: " + videogame.getName() ))
                // Se ejecuta si el flujo se cancela antes de completarse. Puede ser por subscription.cancel(),
                // dispose(), o por un operador como take(n)
                .doOnCancel(() -> System.out.println("[DoOnCancel]"))
                .doOnError(e -> log.error("[DoOnError]", e))
                // Solo se ejecuta si salio ok
                .doOnComplete(() -> System.out.println("[DoOnComplete]"))
                // Se ejecuta cuando el flujo termina, sea por éxito o error. Es similar a doOnComplete + doOnError
                .doOnTerminate(() -> System.out.println("[DoOnTerminate]"))
                // Se ejecuta al final del flujo, indicando la razón (CANCEL, COMPLETE, ERROR)
                .doFinally(s -> System.out.println("[DoFinally]: " + s));
    }
}
