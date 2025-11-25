package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long > {
    List<Atividade> findByAutorId(Long autorId);

    List<Atividade> findBySalaDeAulaId(Long salaId);
}
