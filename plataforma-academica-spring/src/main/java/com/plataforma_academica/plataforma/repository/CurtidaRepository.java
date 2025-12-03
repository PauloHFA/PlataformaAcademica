package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Curtida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurtidaRepository extends JpaRepository<Curtida, Long> {
    Optional<Curtida> findByUsuarioIdAndPostagemId(Long usuarioId, Long postagemId);
    boolean existsByUsuarioIdAndPostagemId(Long usuarioId, Long postagemId);
    void deleteByUsuarioIdAndPostagemId(Long usuarioId, Long postagemId);
}
