package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Frequencia;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface do serviço de Frequência acadêmica.
 * 
 * Camada: Application / Business Service (Academic Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see FrequenciaServiceImpl
 * @see REQ-025 (Controle de Frequência)
 */
public interface FrequenciaService {
    Frequencia registrarFrequencia(Long alunoId, Long salaId, LocalDate data, Boolean presente, String justificativa);

    List<Frequencia> buscarFrequencias(Long alunoId, Long salaId, LocalDate inicio, LocalDate fim);

    List<Frequencia> buscarFrequencias(Long alunoId, Long salaId);

    double calcularPercentualPresenca(Long alunoId, Long salaId, LocalDate inicio, LocalDate fim);
}
