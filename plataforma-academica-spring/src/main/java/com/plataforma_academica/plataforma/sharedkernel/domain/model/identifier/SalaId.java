package com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier;

import java.util.UUID;

/**
 * Value Object imutável de Identidade Única da Sala de Aula Virtual.
 */
public record SalaId(UUID valor) implements Identificador {

    public SalaId {
        if (valor == null) {
            throw new IllegalArgumentException("O valor do SalaId não pode ser nulo.");
        }
    }

    public static SalaId novo() {
        return new SalaId(UUID.randomUUID());
    }

    public static SalaId de(UUID valor) {
        return new SalaId(valor);
    }

    public static SalaId de(String uuidString) {
        if (uuidString == null || uuidString.isBlank()) {
            throw new IllegalArgumentException("A string UUID do SalaId não pode ser vazia.");
        }
        return new SalaId(UUID.fromString(uuidString));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
