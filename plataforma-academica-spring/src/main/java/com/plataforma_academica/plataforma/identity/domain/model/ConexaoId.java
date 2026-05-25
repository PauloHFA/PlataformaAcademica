package com.plataforma_academica.plataforma.identity.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa a identidade única de uma Conexão de Amizade.
 * Imutável.
 */
public final class ConexaoId {
    private final UUID valor;

    private ConexaoId(UUID valor) {
        if (valor == null) {
            throw new IllegalArgumentException("ConexaoId não pode ser nulo");
        }
        this.valor = valor;
    }

    public static ConexaoId novo() {
        return new ConexaoId(UUID.randomUUID());
    }

    public static ConexaoId de(UUID valor) {
        return new ConexaoId(valor);
    }

    public static ConexaoId de(String valor) {
        return new ConexaoId(UUID.fromString(valor));
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
        ConexaoId that = (ConexaoId) o;
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