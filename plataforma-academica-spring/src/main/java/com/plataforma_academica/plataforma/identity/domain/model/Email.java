package com.plataforma_academica.plataforma.identity.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object que representa um endereço de e-mail válido.
 * Imutável com autovalidação sintática RFC 5322 simplificada.
 */
public final class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private final String endereco;

    private Email(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail não pode ser nulo ou vazio");
        }
        String normalizado = endereco.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(normalizado).matches()) {
            throw new IllegalArgumentException("Formato de e-mail inválido: " + endereco);
        }
        this.endereco = normalizado;
    }

    public static Email de(String endereco) {
        return new Email(endereco);
    }

    public String endereco() {
        return endereco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Email email = (Email) o;
        return Objects.equals(endereco, email.endereco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endereco);
    }

    @Override
    public String toString() {
        return endereco;
    }
}