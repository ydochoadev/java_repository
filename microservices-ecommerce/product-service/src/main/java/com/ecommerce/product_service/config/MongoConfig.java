package com.ecommerce.product_service.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

// @Configuration le dice a spring que no use la configuracion por defecto
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    // Inyectamos las variables.
    // La sintaxis es: "${NOMBRE_VARIABLE_EN_SPRING}"
    @Value("${spring.mongodb.host}")
    private String host;
    @Value("${spring.mongodb.port}")
    private int port;
    @Value("${spring.mongodb.database}")
    private String database;
    @Value("${spring.mongodb.username}")
    private String username;
    @Value("${spring.mongodb.password}")
    private String password;
    @Value("${spring.mongodb.authentication-database}")
    private String authDatabase;

    @Override
    protected String getDatabaseName() {
        return database; // Nombre de la BD
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        // Usamos las variables inyectadas en lugar de texto fijo
        MongoCredential credential = MongoCredential.createCredential(
                username, authDatabase, password.toCharArray()
        );

        // Agregamos un string format para armar la URL dinamicamente
        String connectionString = String.format("mongodb://%s:%d", host, port);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .credential(credential)
                .build();

        return MongoClients.create(settings);
    }
}
