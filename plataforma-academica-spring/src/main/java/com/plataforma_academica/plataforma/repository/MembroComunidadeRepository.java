package com.plataforma_academica.plataforma.repository;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.MembroComunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para a entidade MembroComunidade.
 * 
 * Camada: Persistence / Repository
 * Contexto de Negócio: Social / Comunidades
 * Padrões aplicados: Spring Data JPA.
 * 
 * @see MembroComunidade
 * @see docs/domain/social_context.md
 */
@Repository
public interface MembroComunidadeRepository extends JpaRepository<MembroComunidade, UUID> {
    List<MembroComunidade> findByComunidadeId(UUID comunidadeId);

    List<MembroComunidade> findByUsuarioId(UUID usuarioId);

    Optional<MembroComunidade> findByUsuarioIdAndComunidadeId(UUID usuarioId, UUID comunidadeId);
}
