package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.DashboardAlunoDTO;
import com.plataforma_academica.plataforma.dto.DashboardSalaDTO;

import java.time.LocalDate;

/**
 * Interface do serviço de Dashboard do Aluno.
 * 
 * Camada: Application / Business Service (Academic Context)
 * Padrões aplicados: Service Layer, Aggregator Pattern.
 * 
 * @see DashboardAlunoServiceImpl
 * @see REQ-030 (Dashboard Acadêmico)
 */
public interface DashboardAlunoService {
    DashboardAlunoDTO obterDashboardAluno(UUID alunoId, UUID salaId, LocalDate inicio, LocalDate fim);

    DashboardSalaDTO obterDashboardSala(UUID salaId, LocalDate inicio, LocalDate fim);
}
