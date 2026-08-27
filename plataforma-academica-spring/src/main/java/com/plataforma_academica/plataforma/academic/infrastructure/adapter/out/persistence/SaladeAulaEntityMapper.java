package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import com.plataforma_academica.plataforma.academic.domain.model.SalaDeAula;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.SalaId;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.UsuarioId;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SaladeAulaEntityMapper {

    public SalaDeAula toDomain(SaladeAulaEntity entity) {
        var membros = entity.getMembrosIds() != null ? entity.getMembrosIds().stream()
                .map(uuid -> SalaDeAula.MembroSala.reconstituir(
                        UsuarioId.de(uuid),
                        SalaDeAula.PapelMembro.ALUNO,
                        java.time.LocalDateTime.now()))
                .collect(Collectors.toList())
                : new java.util.ArrayList<SalaDeAula.MembroSala>();

        return SalaDeAula.reconstituir(
                SalaId.de(entity.getId()),
                entity.getNome(),
                entity.getCodigoSala(),
                UsuarioId.de(entity.getCriadorId()),
                membros,
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now());
    }

    public SaladeAulaEntity toEntity(SalaDeAula domain) {
        SaladeAulaEntity entity = new SaladeAulaEntity();
        entity.setId(domain.id() != null ? domain.id().valor() : null);
        entity.setNome(domain.nome());
        entity.setCodigoSala(domain.codigoSala());
        entity.setCriadorId(domain.criadorId() != null ? domain.criadorId().valor() : null);
        entity.setMembrosIds(
                domain.membros() != null ? domain.membros().stream()
                        .map(m -> m.usuarioId().valor())
                        .collect(Collectors.toList())
                        : null);
        return entity;
    }
}