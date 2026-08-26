package com.plataforma_academica.plataforma.identity.domain.repository;

import com.plataforma_academica.plataforma.identity.domain.model.Usuario;
import com.plataforma_academica.plataforma.identity.domain.model.UsuarioId;
import com.plataforma_academica.plataforma.identity.domain.model.Email;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);

    Optional<Usuario> findById(UsuarioId id);

    Optional<Usuario> findByEmail(Email email);

    boolean existsByEmail(Email email);
}
