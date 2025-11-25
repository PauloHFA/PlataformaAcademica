package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.AmizadeDTO;
import com.plataforma_academica.plataforma.model.Amizade;

import java.util.List;

public interface AmizadeService {
    Amizade enviarSolicitacao(AmizadeDTO amizadeDTO);
    Amizade responderSolicitacao(Long amizadeId, String acao); // "aceitar" ou "recusar"
    void removerAmizade(Long amizadeId);
    List<Amizade> listarSolicitacoesPendentes(Long usuarioId);
    List<Amizade> listarAmigos(Long usuarioId);
}
