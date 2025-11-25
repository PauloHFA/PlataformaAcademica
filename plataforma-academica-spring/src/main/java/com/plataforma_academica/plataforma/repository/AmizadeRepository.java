package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Amizade;
import com.plataforma_academica.plataforma.model.Usuario;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmizadeRepository extends JpaRepository<Amizade, Long> {

    @Query("SELECT a FROM Amizade a " +
            "WHERE (a.solicitante = :user OR a.destinatario = :user) " +
            "AND a.status = :status")
    List<Amizade> findAmizades(@Param("user") Usuario user,
                               @Param("status") Amizade.Status status);

    Optional<Amizade> findBySolicitanteIdAndDestinatarioId(Long solicitanteId, Long destinatarioId);

    List<Amizade> findBySolicitanteIdOrDestinatarioId(Long id1, Long id2);

    List<Amizade> findBySolicitanteOrDestinatarioAndStatus(Usuario usuario, Usuario usuario1, Amizade.Status status);
}