package com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier;

import java.util.UUID;

/**
 * Value Object imutável de Identidade Única da Postagem Social.
 */
public record PostagemId(UUID valor) implements Identificador {

    public PostagemId {
        if (valor == null) {
            throw new IllegalArgumentException("O valor do PostagemId não pode ser nulo.");
        }
    }

    public static PostagemId novo() {
        return new PostagemId(UUID.randomUUID());
    }

    public static PostagemId de(UUID valor) {
        return new PostagemId(valor);
    }

    public static PostagemId de(String uuidString) {
        if (uuidString == null || uuidString.isBlank()) {
            throw new IllegalArgumentException("A string UUID do PostagemId não pode ser vazia.");
        }
        return new PostagemId(UUID.fromString(uuidString));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
