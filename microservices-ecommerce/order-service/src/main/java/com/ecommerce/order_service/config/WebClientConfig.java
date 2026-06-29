package com.ecommerce.order_service.config;

import com.ecommerce.order_service.service.client.InventoryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public InventoryClient inventoryClient(WebClient.Builder builder) {
        // Cliente web para la ruta dinámica de inventory-service (se coloca el nombre que tiene dentro de Eureka)
        WebClient webClient = builder
                .baseUrl("http://INVENTORY-SERVICE")
                .build();
        // Para ejecutar la petición usando webClient
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        // Implementa la interface InventoryClient
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();

        return factory.createClient(InventoryClient.class);
    }
}
