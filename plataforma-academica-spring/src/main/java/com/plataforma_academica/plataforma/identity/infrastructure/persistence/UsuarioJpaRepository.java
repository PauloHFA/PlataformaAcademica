package com.plataforma_academica.plataforma.identity.infrastructure.persistence;

import com.plataforma_academica.plataforma.identity.domain.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}