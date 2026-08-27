package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.InteracaoUsuario;
import com.plataforma_academica.plataforma.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade InteracaoUsuario.
 * 
 * Camada: Persistence / Repository
 * Contexto de Negócio: Social / Recomendação
 * Padrões aplicados: Spring Data JPA, Consultas customizadas (JPQL).
 * 
 * @see InteracaoUsuario
 * @see docs/architecture/ddd.md
 */
@Repository
public interface InteracaoUsuarioRepository extends JpaRepository<InteracaoUsuario, Long> {

    List<InteracaoUsuario> findByUsuarioOrderByDataInteracaoDesc(Usuario usuario);

    @Query("SELECT i FROM InteracaoUsuario i WHERE i.usuario = :usuario AND i.dataInteracao >= :dataInicio ORDER BY i.dataInteracao DESC")
    List<InteracaoUsuario> findByUsuarioAndDataInteracaoAfter(
            @Param("usuario") Usuario usuario,
            @Param("dataInicio") LocalDateTime dataInicio);

    @Query("SELECT i FROM InteracaoUsuario i WHERE i.entidadeTipo = :entidadeTipo AND i.entidadeId = :entidadeId ORDER BY i.dataInteracao DESC")
    List<InteracaoUsuario> findByEntidade(@Param("entidadeTipo") String entidadeTipo,
            @Param("entidadeId") Long entidadeId);

    @Query("SELECT DISTINCT i.usuario FROM InteracaoUsuario i WHERE i.entidadeTipo = :entidadeTipo AND i.entidadeId = :entidadeId")
    List<Usuario> findUsuariosByEntidade(@Param("entidadeTipo") String entidadeTipo,
            @Param("entidadeId") Long entidadeId);
}