package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaladeAulaJpaRepository extends JpaRepository<SaladeAulaEntity, UUID> {
    Optional<SaladeAulaEntity> findByCodigoSala(String codigoSala);
}
