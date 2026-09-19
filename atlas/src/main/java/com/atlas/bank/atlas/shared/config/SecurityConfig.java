package com.atlas.bank.atlas.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;

// @EnableWebSecurity => Habilitar seguridad web, pero viene por defecto
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        // Accounts
                        .requestMatchers(HttpMethod.POST, "/api/v1/accounts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/accounts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/accounts/{id}").hasAnyRole("USER", "ADMIN")
                        // Transactions
                        .requestMatchers(HttpMethod.POST, "/api/v1/transactions/transfer").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/transactions/{id}/transactions").hasAnyRole("USER", "ADMIN")
                        // H2
                        .requestMatchers("/h2-console/**").permitAll()
                        // Las demás request necesitan autenticarse
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                // Asociado a h2: h2-console se renderiza dentro de un frame y spring lo bloquea
                // Con esta config, permite que esta app se muestre dentro del frame
                .headers(headers -> headers
                        .frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                // Protección contra ataques donde alguien ejecuta acciones en nombre del usuario
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // Conversor personalizado: Los roles están en formato keycloak y ese formato no se puede usar
    // Por ello se crea el converter para transformarlo a SimpleGrantedAuthority
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // Sacar los roles para poder convertirlos
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess == null || realmAccess.get("roles") == null) {
                return List.of(); // Si no hay roles, no se asigna ninguno
            }
            var roles = (List<String>) realmAccess.get("roles");

            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });

        return converter;
    }
}
