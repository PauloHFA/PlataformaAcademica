package com.plataforma_academica.plataforma.repository;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para a entidade {@link Atividade}.
 *
 * Camada: Persistence / Repository Pattern (Academic Context)
 * Consultas derivadas por autor e sala de aula.
 *
 * @see com.plataforma_academica.plataforma.model.Atividade
 * @see docs/domain/academic_context.md
 * @see REQ-020 (Criação de Atividades)
 */
@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, UUID> {
    List<Atividade> findByAutorId(UUID autorId);

    List<Atividade> findBySalaDeAulaId(UUID salaId);
}
