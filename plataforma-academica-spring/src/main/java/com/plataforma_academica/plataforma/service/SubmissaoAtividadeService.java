package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeDTO;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;

import java.util.List;

/**
 * Interface do serviço de Submissões de Atividades.
 * 
 * Camada: Application / Business Service (Academic Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see SubmissaoAtividade
 * @see REQ-026 (Submissão e Avaliação de Atividades)
 */
public interface SubmissaoAtividadeService {

    SubmissaoAtividade enviarSubmissao(UUID atividadeId, UUID alunoId, SubmissaoAtividade submissao);

    SubmissaoAtividade enviarSubmissao(UUID atividadeId, UUID alunoId, SubmissaoAtividadeDTO submissao);

    SubmissaoAtividade enviarSubmissaoComArquivo(UUID atividadeId, UUID alunoId, String descricao,
            org.springframework.web.multipart.MultipartFile arquivo);

    List<SubmissaoAtividade> listarSubmissoesPorAtividade(UUID atividadeId);

    List<SubmissaoAtividade> listarSubmissoesPorAluno(UUID alunoId);

    List<SubmissaoAtividade> listarSubmissoesPorAlunoESala(UUID alunoId, UUID salaId);

    SubmissaoAtividade buscarSubmissaoDoAluno(UUID atividadeId, UUID alunoId);

    SubmissaoAtividade corrigirSubmissao(UUID submissaoId, Double nota, String feedback);

    SubmissaoAtividade marcarComoRecebida(UUID submissaoId);
}