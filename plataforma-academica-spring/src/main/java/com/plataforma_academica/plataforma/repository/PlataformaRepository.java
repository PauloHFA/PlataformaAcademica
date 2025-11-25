package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Plataforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event;

@Repository
public interface PlataformaRepository extends JpaRepository <Plataforma, Long>{
}
