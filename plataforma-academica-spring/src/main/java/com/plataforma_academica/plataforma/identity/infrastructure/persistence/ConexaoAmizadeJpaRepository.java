package com.plataforma_academica.plataforma.identity.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConexaoAmizadeJpaRepository extends JpaRepository<ConexaoAmizadeEntity, String> {
    Optional<ConexaoAmizadeEntity> findBySolicitanteIdAndDestinatarioId(String solicitanteId, String destinatarioId);
}
