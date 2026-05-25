package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import com.plataforma_academica.plataforma.academic.domain.model.SalaDeAula;
import org.springframework.stereotype.Component;

@Component
public class SaladeAulaEntityMapper {

    public SalaDeAula toDomain(SaladeAulaEntity entity) {
        SalaDeAula sala = new SalaDeAula(
                entity.getId(),
                entity.getNome(),
                entity.getCodigoSala(),
                entity.getCriadorId());
        sala.setMembrosIds(entity.getMembrosIds());
        return sala;
    }

    public SaladeAulaEntity toEntity(SalaDeAula domain) {
        SaladeAulaEntity entity = new SaladeAulaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setCodigoSala(domain.getCodigoSala());
        entity.setCriadorId(domain.getCriadorId());
        entity.setMembrosIds(domain.getMembrosIds());
        return entity;
    }
}