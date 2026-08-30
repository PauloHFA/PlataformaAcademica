package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.ComunidadeDTO;
import com.plataforma_academica.plataforma.model.Comunidade;
import com.plataforma_academica.plataforma.model.MembroComunidade;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Interface do serviço de Comunidades.
 * 
 * Camada: Application / Business Service (Social Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see Comunidade
 * @see REQ-015 (Criação de Comunidades)
 */
public interface ComunidadeService {
    Comunidade criarComunidade(@Valid ComunidadeDTO comunidadeDTO);

    void deletarComunidade(UUID id, UUID solicitanteId);

    MembroComunidade entrarComunidade(UUID comunidadeId, UUID usuarioId);

    void sairComunidade(UUID comunidadeId, UUID usuarioId);

    List<Comunidade> listarTodas();

    List<MembroComunidade> listarMembros(UUID comunidadeId);
}
