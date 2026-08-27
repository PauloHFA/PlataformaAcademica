package com.plataforma_academica.plataforma.service;

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

    void deletarComunidade(Long id, Long solicitanteId);

    MembroComunidade entrarComunidade(Long comunidadeId, Long usuarioId);

    void sairComunidade(Long comunidadeId, Long usuarioId);

    List<Comunidade> listarTodas();

    List<MembroComunidade> listarMembros(Long comunidadeId);
}
