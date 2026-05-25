package com.plataforma_academica.plataforma.dto;

import lombok.Data;

@Data
public class AlunoDashboardResumoDTO {
    private Long alunoId;
    private String alunoNome;
    private Integer totalSubmissoes;
    private Integer totalSubmissoesComNota;
    private Double mediaNota;
    private Double percentualPresenca;
}
