package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.DashboardAlunoDTO;

import java.time.LocalDate;

public interface DashboardAlunoService {
    DashboardAlunoDTO obterDashboardAluno(Long alunoId, Long salaId, LocalDate inicio, LocalDate fim);
}
