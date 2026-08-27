package com.plataforma_academica.plataforma.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * ==========================================================================================================
 * WEB CONFIG — Plataforma Acadêmica
 * ==========================================================================================================
 * Configurações globais do Spring MVC (mapeamentos, interceptors e
 * manipuladores de recursos).
 * 
 * Camada: Infrastructure / Web Configuration
 * ==========================================================================================================
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ResourceHandler removido - usando FileController

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
