package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    List<Perfil> findByCurso(String curso);
}
