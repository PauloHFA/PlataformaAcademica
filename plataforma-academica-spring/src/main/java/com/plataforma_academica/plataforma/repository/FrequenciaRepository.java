package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Frequencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório JPA para a entidade {@link Frequencia}.
 *
 * Camada: Persistence / Repository Pattern (Academic Context)
 * Consultas derivadas por aluno, sala e intervalo de datas.
 *
 * @see com.plataforma_academica.plataforma.model.Frequencia
 * @see REQ-025 (Controle de Frequência)
 */
@Repository
public interface FrequenciaRepository extends JpaRepository<Frequencia, Long> {
    List<Frequencia> findByAlunoIdAndSalaDeAulaIdAndDataBetween(Long alunoId, Long salaId, LocalDate inicio,
            LocalDate fim);

    List<Frequencia> findByAlunoIdAndSalaDeAulaId(Long alunoId, Long salaId);
}
