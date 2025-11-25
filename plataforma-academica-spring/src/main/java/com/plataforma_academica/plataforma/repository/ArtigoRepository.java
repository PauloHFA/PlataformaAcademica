package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Artigo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtigoRepository extends JpaRepository<Artigo, Long> {
    List<Artigo> findByAutorId(Long autorId);
}
