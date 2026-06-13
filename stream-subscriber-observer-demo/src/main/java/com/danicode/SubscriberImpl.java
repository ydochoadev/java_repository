package com.danicode;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import java.util.function.Function;

@Log
@AllArgsConstructor
public class SubscriberImpl<T> implements Subscriber<T> {

    // Function -> Para transformar datos
    // Es una función que recibe un objeto de tipo T y devuelve un String.
    private final Function<T, String> mapper;
    private final String name;

    @Override
    public void onNext(T next) {
        final var valueMapper = this.mapper.apply(next); // Aplica la transformación
        log.info("[onNext] " + next);
        log.info("[onNext] mapper" + valueMapper);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
