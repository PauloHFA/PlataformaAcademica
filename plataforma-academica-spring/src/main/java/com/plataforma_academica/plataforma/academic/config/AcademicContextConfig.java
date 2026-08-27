package com.plataforma_academica.plataforma.academic.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * ==========================================================================================================
 * ACADEMIC CONTEXT CONFIG — Plataforma Acadêmica
 * ==========================================================================================================
 * Configuração de componente scan do Bounded Context Acadêmico (DDD).
 * 
 * Camada: Infrastructure / DDD Context Configuration
 * ==========================================================================================================
 */
@Configuration
@ComponentScan(basePackages = "com.plataforma_academica.plataforma.academic")
public class AcademicContextConfig {
}
