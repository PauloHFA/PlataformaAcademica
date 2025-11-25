package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}

