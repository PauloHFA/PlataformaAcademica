package com.plataforma_academica.plataforma.identity.domain.port;

import com.plataforma_academica.plataforma.identity.domain.model.SenhaHash;

/**
 * Porta de saída (Outbound Port) para hashing de senhas.
 * Implementada pela infraestrutura (ex: BCrypt).
 */
public interface PasswordHasherPort {
    SenhaHash hash(String senhaEmTextoPuro);

    boolean matches(String senhaEmTextoPuro, SenhaHash hashArmazenado);
}