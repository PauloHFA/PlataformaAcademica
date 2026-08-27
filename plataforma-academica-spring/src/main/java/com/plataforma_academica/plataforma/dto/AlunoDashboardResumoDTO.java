package com.plataforma_academica.plataforma.dto;

import lombok.Data;

/**
 * DTO resumido de dashboard do aluno.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see DashboardAlunoService
 * @see REQ-030 (Dashboard Acadêmico)
 */
@Data
public class AlunoDashboardResumoDTO {
    private Long alunoId;
    private String alunoNome;
    private Integer totalSubmissoes;
    private Integer totalSubmissoesComNota;
    private Double mediaNota;
    private Double percentualPresenca;
}
