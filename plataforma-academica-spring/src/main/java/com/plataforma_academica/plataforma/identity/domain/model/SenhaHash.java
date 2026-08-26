package com.plataforma_academica.plataforma.identity.domain.model;

import java.util.Objects;

/**
 * Value Object que encapsula o hash de uma senha.
 * Garante que senhas em texto puro nunca existam no modelo de domínio.
 * Imutável.
 */
public final class SenhaHash {
    private final String hash;

    private SenhaHash(String hash) {
        if (hash == null || hash.trim().isEmpty()) {
            throw new IllegalArgumentException("Hash de senha não pode ser nulo ou vazio");
        }
        this.hash = hash;
    }

    public static SenhaHash de(String hash) {
        return new SenhaHash(hash);
    }

    public String hash() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SenhaHash that = (SenhaHash) o;
        return Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }

    @Override
    public String toString() {
        return "[PROTEGIDO]";
    }
}