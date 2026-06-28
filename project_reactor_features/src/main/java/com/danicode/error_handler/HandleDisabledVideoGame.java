package com.danicode.error_handler;

import com.danicode.database.Database;
import com.danicode.models.Console;
import com.danicode.models.Videogame;
import reactor.core.publisher.Flux;

import java.util.List;

public class HandleDisabledVideoGame {

    private static final Videogame DEFAULT_VIDEO_GAME = Videogame.builder()
            .name("Default Game")
            .price(0.0)
            .console(Console.ALL)
            .reviews(List.of())
            .officialWebsite("https://www.default.com")
            .isDiscount(true)
            .totalSold(0)
            .build();

    public static Flux<Videogame> handleDisabledVideoGameDefault() {
        return Database.getDataAsFlux()
                .handle((videogame, sink) -> {
                    if (Console.DISABLED == videogame.getConsole()) {
                        sink.error(new RuntimeException("Video Game Disabled"));
                        return;
                    }
                    sink.next(videogame);
                })
                .onErrorReturn(DEFAULT_VIDEO_GAME)
                .cast(Videogame.class);
    }

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
