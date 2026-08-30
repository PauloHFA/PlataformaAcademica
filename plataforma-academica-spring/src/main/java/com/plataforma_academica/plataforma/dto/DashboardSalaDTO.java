package com.plataforma_academica.plataforma.dto;
import java.util.UUID;

import lombok.Data;

import java.util.List;

@Data
public class DashboardSalaDTO {
    private UUID salaId;
    private String salaNome;
    private Integer totalAtividades;
    private Integer totalSubmissoes;
    private Integer totalSubmissoesComNota;
    private Double mediaNotaSala;
    private Integer totalPresencas;
    private Integer totalFaltas;
    private Double percentualPresenca;
    private List<AlunoDashboardResumoDTO> alunos;
}
