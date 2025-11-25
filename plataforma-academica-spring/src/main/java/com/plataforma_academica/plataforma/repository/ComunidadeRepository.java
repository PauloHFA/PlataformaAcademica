package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Comunidade;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComunidadeRepository extends JpaRepository<Comunidade, Long> {
}
