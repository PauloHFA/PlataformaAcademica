package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.SubmissaoAtividade;

import java.util.List;

public interface SubmissaoAtividadeService {

    SubmissaoAtividade enviarSubmissao(Long atividadeId, Long alunoId, SubmissaoAtividade submissao);

    List<SubmissaoAtividade> listarSubmissoesPorAtividade(Long atividadeId);

    SubmissaoAtividade buscarSubmissaoDoAluno(Long atividadeId, Long alunoId);

    SubmissaoAtividade corrigirSubmissao(Long submissaoId, Double nota, String feedback);
}