package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade Postagem.
 * 
 * Camada: Persistence / Repository
 * Contexto de Negócio: Feed social acadêmico
 * Padrões aplicados: Spring Data JPA, Derived Queries.
 * 
 * @see Postagem
 */
@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {
    List<Postagem> findByTituloContainingIgnoreCase(String titulo);
}
