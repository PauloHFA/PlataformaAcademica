package com.plataforma_academica.plataforma.sharedkernel.domain.exception;

/**
 * Exceção base imutável para todas as violações de regras de negócio e invariantes de domínio.
 * Todas as exceções específicas de domínio devem herdar desta classe.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
