package com.plataforma_academica.plataforma.repository;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para a entidade Professor.
 * 
 * Camada: Persistence / Repository
 * Contexto de Negócio: Docentes e matrícula institucional
 * Padrões aplicados: Spring Data JPA.
 * 
 * @see Professor
 */
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
    Optional<Professor> findByEmail(String email);

    Optional<Professor> findByMatricula(String matricula);
}
