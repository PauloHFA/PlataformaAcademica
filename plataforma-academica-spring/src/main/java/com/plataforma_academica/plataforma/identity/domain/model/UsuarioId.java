package com.plataforma_academica.plataforma.identity.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa a identidade única de um Usuário.
 * Imutável e com validação de integridade.
 */
public final class UsuarioId {
    private final UUID valor;

    private UsuarioId(UUID valor) {
        if (valor == null) {
            throw new IllegalArgumentException("UsuarioId não pode ser nulo");
        }
        this.valor = valor;
    }

    public static UsuarioId novo() {
        return new UsuarioId(UUID.randomUUID());
    }

    public static UsuarioId de(UUID valor) {
        return new UsuarioId(valor);
    }

    public static UsuarioId de(String valor) {
        return new UsuarioId(UUID.fromString(valor));
    }

    public UUID valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UsuarioId that = (UsuarioId) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}