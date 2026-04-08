package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Frequencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FrequenciaRepository extends JpaRepository<Frequencia, Long> {
    List<Frequencia> findByAlunoIdAndSalaDeAulaIdAndDataBetween(Long alunoId, Long salaId, LocalDate inicio, LocalDate fim);
    List<Frequencia> findByAlunoIdAndSalaDeAulaId(Long alunoId, Long salaId);
}
