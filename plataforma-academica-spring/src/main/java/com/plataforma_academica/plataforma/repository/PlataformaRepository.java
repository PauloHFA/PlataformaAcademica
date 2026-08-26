package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Plataforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event;

/**
 * Repositório Spring Data JPA para a entidade Plataforma.
 * 
 * Camada: Persistence / Repository
 * Contexto de Negócio: Escopo global de plataforma
 * Padrões aplicados: Spring Data JPA.
 * 
 * @see Plataforma
 */
@Repository
public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {
}
