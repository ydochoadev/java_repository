package com.danicode.error_handler;

import com.danicode.database.Database;
import com.danicode.models.Console;
import com.danicode.models.Videogame;
import reactor.core.publisher.Flux;

public class HandleDisabledVideoGame {

    public static Flux<Videogame> handleDisabledVideoGame() {

        return Database.getDataAsFlux()
                .handle((videogame, sink) -> {
                    if (Console.DISABLED == videogame.getConsole()) {
                        sink.error(new RuntimeException("Video Game Disabled"));
                        return;
                    }
                    sink.next(videogame);
                })
                .onErrorResume(error -> {
                    System.out.println("ERROR DETECTED: " + error.getMessage());
                    return Flux.merge(Database.getDataAsFlux(), Database.fluxAssassinsDefault);
                })
                // Para el evitar error por tipo de dato
                .cast(Videogame.class)
                .distinct(Videogame::getName);
    }
}
