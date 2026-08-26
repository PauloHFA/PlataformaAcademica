package com.plataforma_academica.plataforma.identity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.plataforma_academica.plataforma.identity.domain.config.IdentityDomainConfig;
import com.plataforma_academica.plataforma.identity.application.config.IdentityApplicationConfig;
import com.plataforma_academica.plataforma.identity.infrastructure.config.IdentityInfrastructureConfig;

/**
 * ==========================================================================================================
 * IDENTITY CONTEXT CONFIG — Plataforma Acadêmica
 * ==========================================================================================================
 * Configuração raiz do Bounded Context de Identidade (DDD).
 * 
 * Camada: Infrastructure / DDD Context Configuration
 * Responsabilidades: Importar e unificar as configurações de Domínio, Aplicação
 * e Infraestrutura do contexto.
 * ==========================================================================================================
 */
@Configuration
@Import({
        IdentityDomainConfig.class,
        IdentityApplicationConfig.class,
        IdentityInfrastructureConfig.class
})
public class IdentityContextConfig {
}