package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.DashboardAlunoDTO;
import com.plataforma_academica.plataforma.dto.DashboardSalaDTO;

import java.time.LocalDate;

public interface DashboardAlunoService {
    DashboardAlunoDTO obterDashboardAluno(Long alunoId, Long salaId, LocalDate inicio, LocalDate fim);
    DashboardSalaDTO obterDashboardSala(Long salaId, LocalDate inicio, LocalDate fim);
}
