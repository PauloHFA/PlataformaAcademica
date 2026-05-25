package com.plataforma_academica.plataforma.sharedkernel.domain.exception;

/**
 * Lançada quando um endereço de e-mail não atende ao padrão RFC 5322 ou está
 * vazio.
 */
public class EmailInvalidoException extends DomainException {

    public EmailInvalidoException(String message) {
        super(message);
    }
}
