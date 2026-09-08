package com.plataforma_academica.plataforma.academic.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence")
public class AcademicInfrastructureConfig {
}
