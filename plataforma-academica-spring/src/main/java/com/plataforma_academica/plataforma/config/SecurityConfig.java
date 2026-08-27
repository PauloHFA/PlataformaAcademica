package com.plataforma_academica.plataforma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ==========================================================================================================
 * SECURITY CONFIG — Plataforma Acadêmica
 * ==========================================================================================================
 * Configuração de segurança da aplicação Spring Security.
 * 
 * Camada: Infrastructure / Security Configuration
 * Responsabilidades: Definir regras de autorização de requisições HTTP, filtros
 * CSRF e políticas de acesso.
 * 
 * Regras:
 * - Desativa CSRF para APIs REST stateless.
 * - Libera acesso a recursos estáticos/uploads e endpoints públicos.
 * 
 * @see REQ-001 (Autenticação e Autorização)
 *      ==========================================================================================================
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/uploads/**").permitAll()
                        .anyRequest().permitAll());
        return http.build();
    }
}
