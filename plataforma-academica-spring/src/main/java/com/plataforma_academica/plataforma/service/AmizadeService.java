package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.AmizadeDTO;
import com.plataforma_academica.plataforma.model.Amizade;

import java.util.List;

public interface AmizadeService {
    Amizade enviarSolicitacao(AmizadeDTO amizadeDTO);
    Amizade responderSolicitacao(UUID amizadeId, String acao); // "aceitar" ou "recusar"
    void removerAmizade(UUID amizadeId);
    List<Amizade> listarSolicitacoesPendentes(UUID usuarioId);
    List<Amizade> listarAmigos(UUID usuarioId);
}
