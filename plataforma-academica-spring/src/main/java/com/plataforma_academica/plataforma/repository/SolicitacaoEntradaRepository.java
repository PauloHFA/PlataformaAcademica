package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.SolicitacaoEntrada;
import com.plataforma_academica.plataforma.model.SolicitacaoEntrada.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitacaoEntradaRepository extends JpaRepository<SolicitacaoEntrada, Long> {
    List<SolicitacaoEntrada> findBySalaIdAndStatus(Long salaId, StatusSolicitacao status);
    Optional<SolicitacaoEntrada> findBySalaIdAndUsuarioIdAndStatus(Long salaId, Long usuarioId, StatusSolicitacao status);
    List<SolicitacaoEntrada> findByUsuarioIdAndStatus(Long usuarioId, StatusSolicitacao status);
}
