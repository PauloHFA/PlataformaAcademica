package com.plataforma_academica.plataforma.repository;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.SolicitacaoEntrada;
import com.plataforma_academica.plataforma.model.SolicitacaoEntrada.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para gerenciamento de solicitações de entrada em
 * salas de aula.
 * 
 * Camada: Infrastructure / Persistence Repository
 * 
 * @see SolicitacaoEntrada
 * @see REQ-022 (Controle de Acesso a Salas de Aula)
 */
@Repository
public interface SolicitacaoEntradaRepository extends JpaRepository<SolicitacaoEntrada, UUID> {
    List<SolicitacaoEntrada> findBySalaIdAndStatus(UUID salaId, StatusSolicitacao status);

    Optional<SolicitacaoEntrada> findBySalaIdAndUsuarioIdAndStatus(UUID salaId, UUID usuarioId,
            StatusSolicitacao status);

    List<SolicitacaoEntrada> findByUsuarioIdAndStatus(UUID usuarioId, StatusSolicitacao status);
}
