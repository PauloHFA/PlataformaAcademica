package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Comunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade {@link Comunidade}.
 *
 * Camada: Persistence / Repository Pattern (Social Context)
 * Fornece operações CRUD para comunidades de interesse.
 *
 * @see com.plataforma_academica.plataforma.model.Comunidade
 * @see docs/domain/social_context.md
 * @see REQ-015 (Criação de Comunidades)
 */
@Repository
public interface ComunidadeRepository extends JpaRepository<Comunidade, Long> {
}
