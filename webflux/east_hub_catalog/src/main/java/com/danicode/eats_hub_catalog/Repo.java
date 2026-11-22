package com.danicode.eats_hub_catalog;

import com.danicode.eats_hub_catalog.collections.RestaurantCollection;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface Repo extends ReactiveMongoRepository<RestaurantCollection, UUID> {
}
