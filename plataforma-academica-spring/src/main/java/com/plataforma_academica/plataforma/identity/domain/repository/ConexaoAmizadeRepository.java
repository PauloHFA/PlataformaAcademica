package com.plataforma_academica.plataforma.identity.domain.repository;

import com.plataforma_academica.plataforma.identity.domain.model.ConexaoAmizade;
import com.plataforma_academica.plataforma.identity.domain.model.ConexaoId;
import com.plataforma_academica.plataforma.identity.domain.model.UsuarioId;
import com.plataforma_academica.plataforma.identity.domain.model.StatusAmizade;

import java.util.List;
import java.util.Optional;

public interface ConexaoAmizadeRepository {
    ConexaoAmizade save(ConexaoAmizade conexao);

    Optional<ConexaoAmizade> findById(ConexaoId id);

    Optional<ConexaoAmizade> findEntreUsuarios(UsuarioId u1, UsuarioId u2);

    List<ConexaoAmizade> findByUsuarioId(UsuarioId usuarioId);

    List<ConexaoAmizade> findByUsuarioIdAndStatus(UsuarioId usuarioId, StatusAmizade status);

    boolean existsBySolicitanteIdAndDestinatarioId(UsuarioId solicitanteId, UsuarioId destinatarioId);
}