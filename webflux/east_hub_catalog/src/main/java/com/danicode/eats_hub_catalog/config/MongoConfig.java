package com.danicode.eats_hub_catalog.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableReactiveMongoRepositories("com.danicode.eats_hub_catalog")
@PropertySource("classpath:mongo_connection.properties")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${mongodb.host}")
    private String host;

    @Value("${mongodb.port}")
    private Integer port;

    @Value("${mongodb.database}")
    private String database;

    @Value("${mongodb.username}")
    private String username;

    @Value("${mongodb.password}")
    private String password;

    @Value("${mongodb.authentication-database}")
    private String authSource;

    @Value("${mongodb.maxPoolSize}")
    private Integer maxPoolSize;

    @Value("${mongodb.minPoolSize}")
    private Integer minPoolSize;

    @Value("${mongodb.maxConnectionLifeTime}")
    private Integer maxConnectionLifetime;

    @Override
    protected String getDatabaseName() {
        return this.database;
    }

    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        final ConnectionString ConnectionString =
                new ConnectionString(String.format("mongodb://%s:%d/%s", this.host, this.port, this.database));
        final MongoCredential credential =
                MongoCredential.createCredential(this.username, this.authSource, this.password.toCharArray());
        final MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(ConnectionString)
                .credential(credential)
                .applyToConnectionPoolSettings(poolSettings -> {
                    poolSettings.maxSize(this.maxPoolSize)
                            .minSize(this.minPoolSize)
                            .maxConnectionLifeTime(this.maxConnectionLifetime, TimeUnit.MILLISECONDS);

                })
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(this.reactiveMongoClient(), getDatabaseName());
    }
}
