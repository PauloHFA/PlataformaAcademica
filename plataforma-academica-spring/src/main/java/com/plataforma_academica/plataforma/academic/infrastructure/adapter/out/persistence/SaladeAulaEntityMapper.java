package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import com.plataforma_academica.plataforma.academic.domain.model.SalaDeAula;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.SalaId;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.UsuarioId;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SaladeAulaEntityMapper {

        public SalaDeAula toDomain(SaladeAulaEntity entity) {
                var membros = entity.getMembros() != null ? entity.getMembros().stream()
                                .map(m -> SalaDeAula.MembroSala.reconstituir(
                                                UsuarioId.de(m.getUsuarioId()),
                                                SalaDeAula.PapelMembro.valueOf(m.getPapel().name()),
                                                m.getDataEntrada()))
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
                // Note: membros are not mapped here as they are managed via SalaMembroEntity
                // directly
                return entity;
        }
}