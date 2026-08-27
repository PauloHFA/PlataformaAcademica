package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SaladeAulaJpaRepository extends JpaRepository<SaladeAulaEntity, UUID> {
    Optional<SaladeAulaEntity> findByCodigoSala(String codigoSala);
}
