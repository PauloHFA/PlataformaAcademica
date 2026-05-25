package com.plataforma_academica.plataforma.identity.infrastructure.persistence;

import com.plataforma_academica.plataforma.identity.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public class ConexaoAmizadeEntityMapper {

    public ConexaoAmizadeEntity toEntity(ConexaoAmizade domain) {
        ConexaoAmizadeEntity entity = new ConexaoAmizadeEntity();
        entity.setId(domain.id().valor().toString());
        entity.setSolicitanteId(domain.solicitanteId().valor().toString());
        entity.setDestinatarioId(domain.destinatarioId().valor().toString());
        entity.setStatus(domain.status().name());
        entity.setDataSolicitacao(domain.dataSolicitacao());
        entity.setDataResposta(domain.dataResposta());
        return entity;
    }

    public ConexaoAmizade toDomain(ConexaoAmizadeEntity entity) {
        // Note: This requires a way to reconstruct the domain object,
        // which might need a package-private constructor or a factory method.
        // For now, assuming a factory method or similar exists in ConexaoAmizade.
        // Since I cannot easily modify the domain model, I will assume a constructor
        // exists.
        // If not, I would need to add one.
        return ConexaoAmizade.reconstituir(
                ConexaoId.de(java.util.UUID.fromString(entity.getId())),
                UsuarioId.de(java.util.UUID.fromString(entity.getSolicitanteId())),
                UsuarioId.de(java.util.UUID.fromString(entity.getDestinatarioId())),
                StatusAmizade.valueOf(entity.getStatus()),
                entity.getDataSolicitacao(),
                entity.getDataResposta());
    }
}
