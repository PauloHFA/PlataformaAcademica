package com.plataforma_academica.plataforma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ==========================================================================================================
 * CORS CONFIG — Plataforma Acadêmica
 * ==========================================================================================================
 * Configuração global de Cross-Origin Resource Sharing (CORS).
 * 
 * Camada: Infrastructure / Web Configuration
 * Responsabilidades: Permitir requisições de origens externas autorizadas (ex:
 * Front-end Angular na porta 4200).
 * 
 * @see REQ-002 (Integração Frontend-Backend)
 *      ==========================================================================================================
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // libera todos os endpoints
                        .allowedOriginPatterns("http://localhost:4200", "http://localhost:5173") // URLs do front-end
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}