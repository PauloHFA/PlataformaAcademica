package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.SaladeAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaladeAulaRepository extends JpaRepository<SaladeAula, Long> {
    Optional<SaladeAula> findById(Long saladeAulaId);
    Optional<SaladeAula> findByCodigoSala(String codigoSala);
    void deleteById(Long saladeAulaId);
}
