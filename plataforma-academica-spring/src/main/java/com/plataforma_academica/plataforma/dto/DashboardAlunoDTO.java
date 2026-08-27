package com.plataforma_academica.plataforma.dto;

import lombok.Data;

import java.util.List;

import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeResponseDTO;

/**
 * DTO de dashboard do aluno.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see DashboardAlunoService
 * @see REQ-030 (Dashboard Acadêmico)
 */
@Data
public class DashboardAlunoDTO {
    private Long alunoId;
    private String alunoNome;
    private Long salaId;
    private String salaNome;

    private Integer totalAtividades;
    private Integer totalSubmissoes;
    private Integer totalSubmissoesComNota;
    private Double mediaNota;

    private Integer totalPresencas;
    private Integer totalFaltas;
    private Double percentualPresenca;

    private List<SubmissaoAtividadeResponseDTO> submissoes;
}
