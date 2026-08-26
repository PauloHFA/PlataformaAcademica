package com.plataforma_academica.plataforma.academic.domain.repository;

import com.plataforma_academica.plataforma.academic.domain.model.SalaDeAula;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.SalaId;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.UsuarioId;
import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório de domínio.
 * Define o contrato para persistência, sem detalhes de implementação (JPA/SQL).
 */
public interface SalaDeAulaRepository {
    void salvar(SalaDeAula salaDeAula);

    Optional<SalaDeAula> buscarPorId(SalaId id);

    Optional<SalaDeAula> buscarPorCodigo(String codigo);

    List<SalaDeAula> buscarPorMembro(UsuarioId usuarioId);

    List<SalaDeAula> buscarPorCriador(UsuarioId criadorId);
}
