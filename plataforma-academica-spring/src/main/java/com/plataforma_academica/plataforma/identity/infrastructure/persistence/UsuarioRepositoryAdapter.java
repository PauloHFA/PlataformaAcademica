package com.plataforma_academica.plataforma.identity.infrastructure.persistence;

import com.plataforma_academica.plataforma.identity.domain.model.Usuario;
import com.plataforma_academica.plataforma.identity.domain.model.UsuarioId;
import com.plataforma_academica.plataforma.identity.domain.model.Email;
import com.plataforma_academica.plataforma.identity.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adaptador de persistência para UsuarioRepository.
 * Implementa a porta de saída usando Spring Data JPA.
 */
@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;
    private final UsuarioEntityMapper mapper;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository, UsuarioEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        UsuarioEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Usuario> findById(UsuarioId id) {
        return jpaRepository.findById(id.valor())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.endereco())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.endereco());
    }
}