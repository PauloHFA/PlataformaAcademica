package com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier;

import java.util.UUID;

/**
 * Contrato comum para todos os Value Objects de Identidade do sistema.
 */
public interface Identificador {
    UUID valor();
}
