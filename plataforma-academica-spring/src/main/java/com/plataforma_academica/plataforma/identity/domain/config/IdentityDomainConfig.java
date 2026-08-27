package com.plataforma_academica.plataforma.identity.domain.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de Component Scan para o domínio de Identidade (DDD).
 * 
 * Camada: Configuration / Domain Context
 * Padrões aplicados: Domain-Driven Design (DDD), Spring Configuration.
 * 
 * @see docs/architecture/ddd.md
 */
@Configuration
@ComponentScan(basePackages = {
                "com.plataforma_academica.plataforma.identity.domain.model",
                "com.plataforma_academica.plataforma.identity.domain.repository",
                "com.plataforma_academica.plataforma.identity.domain.port",
                "com.plataforma_academica.plataforma.identity.domain.event"
})
public class IdentityDomainConfig {
}