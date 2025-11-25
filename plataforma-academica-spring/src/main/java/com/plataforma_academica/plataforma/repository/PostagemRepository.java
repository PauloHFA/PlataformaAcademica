package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {
    List<Postagem> findByTituloContainingIgnoreCase(String titulo);
}
