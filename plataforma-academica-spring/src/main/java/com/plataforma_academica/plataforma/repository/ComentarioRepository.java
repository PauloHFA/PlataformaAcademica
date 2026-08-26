package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório JPA para a entidade {@link Comentario}.
 *
 * Camada: Persistence / Repository Pattern (Social Context)
 * Fornece consultas derivadas por destino (postagem, atividade, sala de aula)
 * e operação de exclusão em cascata por postagem.
 *
 * @see com.plataforma_academica.plataforma.model.Comentario
 * @see docs/domain/social_context.md
 * @see REQ-030 (Sistema de Comentários)
 */
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findBySaladeAulaId(Long salaId);

    List<Comentario> findByAtividadeId(Long atividadeId);

    List<Comentario> findByPostagemId(Long postagemId);

    void deleteByPostagemId(Long postagemId);
}
