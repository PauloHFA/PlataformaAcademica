package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import com.plataforma_academica.plataforma.academic.domain.model.SalaDeAula;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.SalaId;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.UsuarioId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaladeAulaEntityMapper {

    public SalaDeAula toDomain(SaladeAulaEntity entity) {
        List<SalaDeAula.MembroSala> membros = entity.getMembros().stream()
                .map(m -> SalaDeAula.MembroSala.reconstituir(
                        UsuarioId.de(m.getUsuarioId()),
                        SalaDeAula.PapelMembro.valueOf(m.getPapel().name()),
                        m.getDataEntrada()))
                .collect(Collectors.toList());

        return SalaDeAula.reconstituir(
                SalaId.de(entity.getId()),
                entity.getNome(),
                entity.getCodigoSala(),
                UsuarioId.de(entity.getCriadorId()),
                membros,
                entity.getDataCriacao(),
                entity.getDataAtualizacao());
    }

    public SaladeAulaEntity toEntity(SalaDeAula domain) {
        SaladeAulaEntity entity = new SaladeAulaEntity();
        entity.setId(domain.id().valor());
        entity.setNome(domain.nome());
        entity.setCodigoSala(domain.codigoSala());
        entity.setCriadorId(domain.criadorId().valor());
        entity.setDataCriacao(domain.dataCriacao());
        entity.setDataAtualizacao(domain.dataAtualizacao());

        List<SalaMembroEntity> membrosEntity = domain.membros().stream()
                .map(m -> {
                    SalaMembroEntity me = new SalaMembroEntity();
                    me.setUsuarioId(m.usuarioId().valor());
                    me.setPapel(SalaMembroEntity.PapelMembro.valueOf(m.papel().name()));
                    me.setDataEntrada(m.dataEntrada());
                    me.setSala(entity); // Importante para o relacionamento bidirecional
                    return me;
                })
                .collect(Collectors.toList());

        entity.setMembros(membrosEntity);
        return entity;
    }
}