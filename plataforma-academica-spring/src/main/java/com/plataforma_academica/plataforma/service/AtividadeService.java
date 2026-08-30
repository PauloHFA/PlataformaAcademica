package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface do serviço de Atividades acadêmicas.
 * 
 * Camada: Application / Business Service (Academic Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see Atividade
 * @see REQ-020 (Criação de Atividades)
 */
public interface AtividadeService {

    @Transactional
    Atividade criarAtividade(UUID salaId, Atividade atividade, UUID autorId);

    Atividade criarAtividade(UUID salaId, AtividadeDTO atividadeDTO, UUID autorId);

    Atividade buscarAtividadePorId(UUID atividadeId);

    List<Atividade> listarAtividadesPorSala(UUID salaId);

    Atividade atualizarAtividade(UUID atividadeId, Atividade atividadeAtualizada, UUID autorId);

    Atividade atualizarAtividade(UUID atividadeId, AtividadeDTO atividadeDTO, UUID autorId);

    void deletarAtividade(UUID atividadeId, UUID autorId);

    List<Atividade> listarAtividadesPorAutor(UUID autorId);
}
