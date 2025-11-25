package com.plataforma_academica.plataforma.mapper;

import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeDTO;
import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeResponseDTO;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import com.plataforma_academica.plataforma.model.Usuario;

public class SubmissaoAtividadeMapper {

    // ============================================================
    // ENTITY → DTO (básico, usado para criação/edição)
    // ============================================================
    public static SubmissaoAtividadeDTO toDTO(SubmissaoAtividade submissao) {
        if (submissao == null) return null;

        SubmissaoAtividadeDTO dto = new SubmissaoAtividadeDTO();

        dto.setId(submissao.getId());

        dto.setAtividadeId(
                submissao.getAtividade() != null ? submissao.getAtividade().getId() : null
        );

        dto.setAlunoId(
                submissao.getAluno() != null ? submissao.getAluno().getId() : null
        );

        dto.setUrlDocumento(submissao.getUrlDocumento());
        dto.setDataSubmissao(submissao.getDataSubmissao());
        dto.setNota(submissao.getNota());
        dto.setFeedback(submissao.getFeedback());

        return dto;
    }

    // ============================================================
    // ENTITY → RESPONSE DTO (completo para retorno ao front)
    // ============================================================
    public static SubmissaoAtividadeResponseDTO toResponse(SubmissaoAtividade submissao) {
        if (submissao == null) return null;

        SubmissaoAtividadeResponseDTO response = new SubmissaoAtividadeResponseDTO();

        response.setId(submissao.getId());

        // Atividade
        if (submissao.getAtividade() != null) {
            response.setAtividadeId(submissao.getAtividade().getId());
            response.setAtividadeTitulo(submissao.getAtividade().getTitulo());
        }

        // Aluno
        if (submissao.getAluno() != null) {
            response.setAlunoId(submissao.getAluno().getId());
            response.setAlunoNome(submissao.getAluno().getNome());
        }

        response.setUrlDocumento(submissao.getUrlDocumento());
        response.setDataSubmissao(submissao.getDataSubmissao());
        response.setNota(submissao.getNota());
        response.setFeedback(submissao.getFeedback());

        return response;
    }

    // ============================================================
    // DTO → ENTITY (para salvar no banco)
    // ============================================================
    public static SubmissaoAtividade toEntity(
            SubmissaoAtividadeDTO dto,
            Atividade atividade,
            Usuario aluno
    ) {
        if (dto == null) return null;

        SubmissaoAtividade submissao = new SubmissaoAtividade();

        submissao.setId(dto.getId());
        submissao.setAtividade(atividade);
        submissao.setAluno(aluno);

        submissao.setUrlDocumento(dto.getUrlDocumento());
        submissao.setDataSubmissao(dto.getDataSubmissao());
        submissao.setNota(dto.getNota());
        submissao.setFeedback(dto.getFeedback());

        return submissao;
    }
}
