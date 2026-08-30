package com.plataforma_academica.plataforma.repository;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.Artigo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para a entidade {@link Artigo}.
 *
 * Camada: Persistence / Repository Pattern (Academic Context)
 * Consultas derivadas por autor para feed de artigos.
 *
 * @see com.plataforma_academica.plataforma.model.Artigo
 * @see docs/domain/academic_context.md
 * @see REQ-010 (Publicação de Artigos Acadêmicos)
 */
@Repository
public interface ArtigoRepository extends JpaRepository<Artigo, UUID> {
    List<Artigo> findByAutorId(UUID autorId);
}
