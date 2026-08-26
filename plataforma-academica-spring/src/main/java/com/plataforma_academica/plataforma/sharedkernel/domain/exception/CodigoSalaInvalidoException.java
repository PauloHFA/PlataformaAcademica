package com.plataforma_academica.plataforma.sharedkernel.domain.exception;

/**
 * Lançada quando o código de acesso da sala de aula é inválido.
 */
public class CodigoSalaInvalidoException extends DomainException {

    public CodigoSalaInvalidoException(String message) {
        super(message);
    }
}
