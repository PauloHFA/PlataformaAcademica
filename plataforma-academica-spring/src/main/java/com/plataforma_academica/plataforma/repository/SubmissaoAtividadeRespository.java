package com.plataforma_academica.plataforma.repository;
import java.util.UUID;

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
public interface SubmissaoAtividadeRespository extends JpaRepository<SubmissaoAtividade, UUID> {
    SubmissaoAtividade findByAtividadeIdAndAlunoId(UUID atividadeId, UUID alunoId);

    List<SubmissaoAtividade> findByAtividadeId(UUID atividadeId);

    List<SubmissaoAtividade> findByAlunoId(UUID alunoId);

    List<SubmissaoAtividade> findByAlunoIdAndAtividade_SalaDeAula_Id(UUID alunoId, UUID salaId);
}
