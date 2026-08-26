package com.plataforma_academica.plataforma.identity.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.plataforma_academica.plataforma.identity.infrastructure.persistence")
public class IdentityInfrastructureConfig {
}