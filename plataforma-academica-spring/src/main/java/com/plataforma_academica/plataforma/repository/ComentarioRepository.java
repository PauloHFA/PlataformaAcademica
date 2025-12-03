package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findBySaladeAulaId(Long salaId);
    List<Comentario> findByAtividadeId(Long atividadeId);
    List<Comentario> findByPostagemId(Long postagemId);
}

