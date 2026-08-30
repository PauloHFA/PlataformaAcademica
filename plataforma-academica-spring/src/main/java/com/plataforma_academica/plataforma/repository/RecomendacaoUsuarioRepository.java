package com.plataforma_academica.plataforma.repository;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.RecomendacaoUsuario;
import com.plataforma_academica.plataforma.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade RecomendacaoUsuario.
 * 
 * Camada: Persistence / Repository
 * Contexto de Negócio: Motor de recomendação por similaridade
 * Padrões aplicados: Spring Data JPA, JPQL.
 * 
 * @see RecomendacaoUsuario
 * @see docs/architecture/recommendation-engine.md
 */
@Repository
public interface RecomendacaoUsuarioRepository extends JpaRepository<RecomendacaoUsuario, UUID> {

    List<RecomendacaoUsuario> findByUsuarioAndAtivoOrderByScoreSimilaridadeDesc(Usuario usuario, Boolean ativo);

    List<RecomendacaoUsuario> findByUsuarioRecomendadoAndAtivo(Usuario usuarioRecomendado, Boolean ativo);

    @Query("SELECT r FROM RecomendacaoUsuario r WHERE r.usuario = :usuario AND r.tipoRecomendacao = :tipo AND r.ativo = true ORDER BY r.scoreSimilaridade DESC")
    List<RecomendacaoUsuario> findByUsuarioAndTipoRecomendacao(
            @Param("usuario") Usuario usuario,
            @Param("tipo") RecomendacaoUsuario.TipoRecomendacao tipo);

    @Query("SELECT COUNT(r) FROM RecomendacaoUsuario r WHERE r.usuario = :usuario AND r.usuarioRecomendado = :usuarioRecomendado AND r.ativo = true")
    UUID countExistingRecomendacao(@Param("usuario") Usuario usuario,
            @Param("usuarioRecomendado") Usuario usuarioRecomendado);
}