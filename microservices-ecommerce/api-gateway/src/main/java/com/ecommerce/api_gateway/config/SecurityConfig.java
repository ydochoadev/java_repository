package com.ecommerce.api_gateway.config;

import com.ecommerce.api_gateway.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable) // En stateless
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/eureka/**").permitAll() // permitir las rutas de eureka
                        .pathMatchers(HttpMethod.GET, "/api/v1/product/**").permitAll() // ver productos
                        .pathMatchers(HttpMethod.GET, "/api/v1/inventory/**").permitAll() // ver inventario
                        /*
                            Productos e Inventario
                                => hasRole(ADMIN): Función concatena el prefijo ROLE_ por defecto
                                => Para el resto de rutas, es necesario ser ADMIN
                        * */
                        .pathMatchers("api/v1/product/**").hasRole(Role.ADMIN.name())
                        .pathMatchers("api/v1/inventory/**").hasRole(Role.ADMIN.name())
                        // Orders
                        .pathMatchers(HttpMethod.POST, "/api/v1/order/**").hasRole(Role.USER.name())
                        .pathMatchers("api/v1/order/**").hasRole(Role.ADMIN.name()) // ADMIN para ver los pedidos
                        .anyExchange().authenticated()) // cualquier otra debe autenticarse
                // Convierte a la app en un servidor que consume tokens:
                // Activa la guardia (valida el token que llama)
                // Conecta el traductor (reactiveJwtAuthenticationConverter): Porque spring no conoce Keycloak
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(reactiveJwtAuthenticationConverter())));
        return http.build();
    }

    // Traductor de roles
    private ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverter() {
        // Se crea el convertidor (Objeto que realiza el trabajo)
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Capturar el token que está en realm_access, donde están los roles
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            if (realmAccess == null || realmAccess.isEmpty()) {
                return Collections.emptyList();
            }

            Collection<String> roles = (Collection<String>) realmAccess.get("roles");

            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        });
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
