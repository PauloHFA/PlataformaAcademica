package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeDTO;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;

import java.util.List;

public interface SubmissaoAtividadeService {

    SubmissaoAtividade enviarSubmissao(Long atividadeId, Long alunoId, SubmissaoAtividade submissao);
    
    SubmissaoAtividade enviarSubmissao(Long atividadeId, Long alunoId, SubmissaoAtividadeDTO submissao);

    SubmissaoAtividade enviarSubmissaoComArquivo(Long atividadeId, Long alunoId, String descricao, org.springframework.web.multipart.MultipartFile arquivo);

    List<SubmissaoAtividade> listarSubmissoesPorAtividade(Long atividadeId);

    List<SubmissaoAtividade> listarSubmissoesPorAluno(Long alunoId);

    List<SubmissaoAtividade> listarSubmissoesPorAlunoESala(Long alunoId, Long salaId);

    SubmissaoAtividade buscarSubmissaoDoAluno(Long atividadeId, Long alunoId);

    SubmissaoAtividade corrigirSubmissao(Long submissaoId, Double nota, String feedback);

    SubmissaoAtividade marcarComoRecebida(Long submissaoId);
}