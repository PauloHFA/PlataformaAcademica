package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade SubmissaoAtividade.
 * 
 * Camada: Infrastructure / Persistence Repository
 * 
 * @see SubmissaoAtividade
 * @see REQ-026 (Submissão e Avaliação de Atividades)
 */
@Repository
public interface SubmissaoAtividadeRespository extends JpaRepository<SubmissaoAtividade, Long> {
    SubmissaoAtividade findByAtividadeIdAndAlunoId(Long atividadeId, Long alunoId);

    List<SubmissaoAtividade> findByAtividadeId(Long atividadeId);

    List<SubmissaoAtividade> findByAlunoId(Long alunoId);

    List<SubmissaoAtividade> findByAlunoIdAndAtividade_SalaDeAula_Id(Long alunoId, Long salaId);
}
