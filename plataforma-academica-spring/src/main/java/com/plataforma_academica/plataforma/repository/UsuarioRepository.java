package com.plataforma_academica.plataforma.repository;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.plataforma_academica.plataforma.model.Usuario;

import java.util.Optional;

/**
 * Repositório JPA para a entidade {@link Usuario}.
 *
 * Camada: Persistence / Repository Pattern
 * Contexto de Negócio: Acesso a dados de autenticação, perfil e herança
 * (Professor, Admin, Perfil) via JpaRepository.
 *
 * @see com.plataforma_academica.plataforma.model.Usuario
 * @see docs/domain/identity_context.md
 * @see REQ-001 (Autenticação e Perfil de Usuário)
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmailAndSenha(String email, String senha);

    Optional<Usuario> findByEmail(String email);
}