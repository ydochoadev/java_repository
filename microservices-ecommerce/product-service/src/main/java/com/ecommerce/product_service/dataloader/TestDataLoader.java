package com.ecommerce.product_service.dataloader;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// Cuando spring arranca, crea una instancia de esta clase y lo guarda en application context
@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {
    private final ProductRepository productRepository;

    // run => Se ejecuta inmediatamente despues de que spring termina de cargar el contexto
    @Override
    public void run(String... args) throws Exception {
        // Crear producto y guardar en la BD con patron builder
        Product product = Product.builder()
                .name("Samsung Galaxy X11")
                .description("Mi Samsung Galaxy desciption")
                .price(BigDecimal.valueOf(1800))
                .build();

        productRepository.save(product);
        System.out.println("Datos cargados correctamente: " + product.getId());
    }
}
