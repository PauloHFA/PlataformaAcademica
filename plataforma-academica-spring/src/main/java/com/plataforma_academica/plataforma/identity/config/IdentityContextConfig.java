package com.plataforma_academica.plataforma.identity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.plataforma_academica.plataforma.identity.domain.config.IdentityDomainConfig;
import com.plataforma_academica.plataforma.identity.application.config.IdentityApplicationConfig;
import com.plataforma_academica.plataforma.identity.infrastructure.config.IdentityInfrastructureConfig;

@Configuration
@Import({
                IdentityDomainConfig.class,
                IdentityApplicationConfig.class,
                IdentityInfrastructureConfig.class
})
public class IdentityContextConfig {
}