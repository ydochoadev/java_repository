package com.danicode.error_handler;

import com.danicode.database.Database;
import com.danicode.models.Console;
import com.danicode.models.Videogame;
import reactor.core.publisher.Flux;

public class FallbackService {

    public static Flux<Videogame> callFallback() {
        return Database.getDataAsFlux()
                .handle((videogame, sink) -> {
                    if (Console.DISABLED == videogame.getConsole()) {
                        sink.error(new RuntimeException("Video Game Disabled"));
                        return;
                    }
                    sink.next(videogame);
                })
                .retry(5) // reintentar 5 veces llamada a BD por latencia
                .onErrorResume(error -> {
                    System.out.println("ERROR DATABASE: " + error.getMessage());
                    return Database.fluxFallback; // si el error continúa, llamar al fallback
                })
                .repeat(1) // repetir todo el flujo
                // Para el evitar error por tipo de dato
                .cast(Videogame.class);
    }
}
