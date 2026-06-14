package com.danicode.pipelines;

import com.danicode.database.Database;
import com.danicode.models.Videogame;
import reactor.core.publisher.Flux;

public class PipelineTopSelling {

    // return all names of video games with have a sold > 80
    public static Flux<String> getTopSellingVideoGames() {
        return Database.getDataAsFlux()
                .filter(videogame -> videogame.getTotalSold() > 80)
                .map(Videogame::getName);
    }
}
