package com.plataforma_academica.plataforma.identity.domain.model;

/**
 * Exceção lançada quando um usuário tenta criar uma conexão de amizade consigo
 * mesmo.
 */
public class AutoConexaoInvalidaException extends IllegalArgumentException {
    public AutoConexaoInvalidaException(String message) {
        super(message);
    }
}