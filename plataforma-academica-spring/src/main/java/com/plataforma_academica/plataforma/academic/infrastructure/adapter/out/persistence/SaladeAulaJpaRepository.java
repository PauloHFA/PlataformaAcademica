package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SaladeAulaJpaRepository extends JpaRepository<SaladeAulaEntity, UUID> {
    Optional<SaladeAulaEntity> findByCodigoSala(String codigoSala);

    @Query("SELECT s FROM SaladeAulaEntity s JOIN s.membros m WHERE m.usuarioId = ?1")
    List<SaladeAulaEntity> findByMembros_UsuarioId(UUID usuarioId);

    List<SaladeAulaEntity> findByCriadorId(UUID criadorId);
}
