package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Frequencia;

import java.time.LocalDate;
import java.util.List;

public interface FrequenciaService {
    Frequencia registrarFrequencia(Long alunoId, Long salaId, LocalDate data, Boolean presente, String justificativa);
    List<Frequencia> buscarFrequencias(Long alunoId, Long salaId, LocalDate inicio, LocalDate fim);
    List<Frequencia> buscarFrequencias(Long alunoId, Long salaId);
    double calcularPercentualPresenca(Long alunoId, Long salaId, LocalDate inicio, LocalDate fim);
}
