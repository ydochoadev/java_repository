package com.danicode.pipelines;

import com.danicode.database.Database;
import com.danicode.models.Review;
import reactor.core.publisher.Flux;

public class PipelineAllComments {
    public static Flux<String> getAllReviewsComments() {
        // flatMap(videogame -> {}) transforma videogame al objeto Review
        return Database
                .getDataAsFlux()
                .flatMap(videogame -> Flux.fromIterable(videogame.getReviews()))
                .map(Review::getComment);
    }
}
