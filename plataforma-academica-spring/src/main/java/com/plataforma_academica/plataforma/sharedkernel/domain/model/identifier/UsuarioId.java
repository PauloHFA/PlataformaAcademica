package com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier;

import java.util.UUID;

/**
 * Value Object imutável de Identidade Única do Usuário.
 */
public record UsuarioId(UUID valor) implements Identificador {

    public UsuarioId {
        if (valor == null) {
            throw new IllegalArgumentException("O valor do UsuarioId não pode ser nulo.");
        }
    }

    public static UsuarioId novo() {
        return new UsuarioId(UUID.randomUUID());
    }

    public static UsuarioId de(UUID valor) {
        return new UsuarioId(valor);
    }

    public static UsuarioId de(String uuidString) {
        if (uuidString == null || uuidString.isBlank()) {
            throw new IllegalArgumentException("A string UUID do UsuarioId não pode ser vazia.");
        }
        return new UsuarioId(UUID.fromString(uuidString));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
