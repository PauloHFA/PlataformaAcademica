package com.plataforma_academica.plataforma.identity.infrastructure.adapter;

import com.plataforma_academica.plataforma.identity.domain.model.SenhaHash;
import com.plataforma_academica.plataforma.identity.domain.port.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasherAdapter implements PasswordHasherPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public SenhaHash hash(String senhaPura) {
        return SenhaHash.de(encoder.encode(senhaPura));
    }

    @Override
    public boolean matches(String senhaPura, SenhaHash hashExistente) {
        return encoder.matches(senhaPura, hashExistente.hash());
    }
}
