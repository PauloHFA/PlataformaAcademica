package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Plataforma;

import java.util.List;

/**
 * Interface do serviço de Plataforma.
 * 
 * Camada: Application / Business Service
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see Plataforma
 * @see REQ-001 (Configuração da Plataforma)
 */
public interface PlataformaService {

    Plataforma salvar(Plataforma plataforma);

    Plataforma atualizar(Long id, Plataforma plataforma);

    Plataforma buscarPorId(Long id);

    List<Plataforma> listarTudo();

    void deletar(Long id);
}
