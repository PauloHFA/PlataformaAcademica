package com.plataforma_academica.plataforma.identity.application.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * ==========================================================================================================
 * IDENTITY APPLICATION CONFIG — Plataforma Acadêmica
 * ==========================================================================================================
 * Configuração de component scan da camada de Aplicação do contexto de
 * Identidade.
 * ==========================================================================================================
 */
@Configuration
@ComponentScan(basePackages = {
                "com.plataforma_academica.plataforma.identity.application",
                "com.plataforma_academica.plataforma.identity.domain.service"
})
public class IdentityApplicationConfig {
}