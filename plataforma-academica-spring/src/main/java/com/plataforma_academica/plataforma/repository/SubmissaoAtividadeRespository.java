package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissaoAtividadeRespository extends JpaRepository <SubmissaoAtividade, Long> {
    SubmissaoAtividade findByAtividadeIdAndAlunoId(Long atividadeId, Long alunoId);

    List<SubmissaoAtividade> findByAtividadeId(Long atividadeId);
}
