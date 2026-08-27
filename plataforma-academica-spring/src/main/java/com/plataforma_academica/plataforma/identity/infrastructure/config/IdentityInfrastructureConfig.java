package com.plataforma_academica.plataforma.identity.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuração de Repositórios JPA para o contexto de Identidade.
 * 
 * Camada: Configuration / Infrastructure Context
 * Padrões aplicados: Spring Data JPA, Domain-Driven Design (DDD).
 * 
 * @see docs/architecture/ddd.md
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.plataforma_academica.plataforma.identity.infrastructure.persistence")
public class IdentityInfrastructureConfig {
}