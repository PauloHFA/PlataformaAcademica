package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AtividadeService {

    @Transactional
    Atividade criarAtividade(Long salaId, Atividade atividade, Long autorId);

    Atividade criarAtividade(Long salaId, AtividadeDTO atividadeDTO, Long autorId);

    Atividade buscarAtividadePorId(Long atividadeId);
    List<Atividade> listarAtividadesPorSala(Long salaId);
    Atividade atualizarAtividade(Long atividadeId, Atividade atividadeAtualizada, Long autorId);
    Atividade atualizarAtividade(Long atividadeId, AtividadeDTO atividadeDTO, Long autorId);
    void deletarAtividade(Long atividadeId, Long autorId);
    List<Atividade> listarAtividadesPorAutor(Long autorId);
}
