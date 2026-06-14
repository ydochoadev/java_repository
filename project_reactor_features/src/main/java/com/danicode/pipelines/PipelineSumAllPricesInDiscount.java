package com.danicode.pipelines;

import com.danicode.database.Database;
import com.danicode.models.Videogame;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class PipelineSumAllPricesInDiscount {
    // Sum all prices of each video game in discount
    public static Mono<Double> getSumAllPricesInDiscount() {
        return Database.getDataAsFlux()
                .filter(Videogame::getIsDiscount)
                .map(Videogame::getPrice)
                .reduce(0.0, Double::sum);
    }
}
