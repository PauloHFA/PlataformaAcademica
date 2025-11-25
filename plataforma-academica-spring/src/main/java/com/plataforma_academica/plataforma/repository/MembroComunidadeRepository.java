package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.MembroComunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembroComunidadeRepository extends JpaRepository<MembroComunidade, Long> {
    List<MembroComunidade> findByComunidadeId(Long comunidadeId);
    List<MembroComunidade> findByUsuarioId(Long usuarioId);
    Optional<MembroComunidade> findByUsuarioIdAndComunidadeId(Long usuarioId, Long comunidadeId);
}
