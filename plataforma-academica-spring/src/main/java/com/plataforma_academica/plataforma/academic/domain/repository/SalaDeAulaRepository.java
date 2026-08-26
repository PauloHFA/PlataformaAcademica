package com.plataforma_academica.plataforma.academic.domain.repository;

import com.plataforma_academica.plataforma.academic.domain.model.SalaDeAula;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repositório de domínio.
 * Define o contrato para persistência, sem detalhes de implementação (JPA/SQL).
 */
public interface SalaDeAulaRepository {
    void salvar(SalaDeAula salaDeAula);

    Optional<SalaDeAula> buscarPorId(UUID id);

    Optional<SalaDeAula> buscarPorCodigo(String codigo);
}
