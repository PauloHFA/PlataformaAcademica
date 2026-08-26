package com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier;

import java.util.UUID;

/**
 * Value Object imutável de Identidade Única da Atividade Acadêmica.
 */
public record AtividadeId(UUID valor) implements Identificador {

    public AtividadeId {
        if (valor == null) {
            throw new IllegalArgumentException("O valor do AtividadeId não pode ser nulo.");
        }
    }

    public static AtividadeId novo() {
        return new AtividadeId(UUID.randomUUID());
    }

    public static AtividadeId de(UUID valor) {
        return new AtividadeId(valor);
    }

    public static AtividadeId de(String uuidString) {
        if (uuidString == null || uuidString.isBlank()) {
            throw new IllegalArgumentException("A string UUID do AtividadeId não pode ser vazia.");
        }
        return new AtividadeId(UUID.fromString(uuidString));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
