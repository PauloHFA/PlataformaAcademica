package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import com.plataforma_academica.plataforma.academic.domain.model.SalaDeAula;
import com.plataforma_academica.plataforma.academic.domain.repository.SalaDeAulaRepository;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.SalaId;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.UsuarioId;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SalaDeAulaRepositoryAdapter implements SalaDeAulaRepository {

    private final SaladeAulaJpaRepository jpaRepository;
    private final SaladeAulaEntityMapper mapper;

    public SalaDeAulaRepositoryAdapter(SaladeAulaJpaRepository jpaRepository, SaladeAulaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void salvar(SalaDeAula salaDeAula) {
        jpaRepository.save(mapper.toEntity(salaDeAula));
    }

    @Override
    public Optional<SalaDeAula> buscarPorId(SalaId id) {
        return jpaRepository.findById(id.valor()).map(mapper::toDomain);
    }

    @Override
    public Optional<SalaDeAula> buscarPorCodigo(String codigo) {
        return jpaRepository.findByCodigoSala(codigo).map(mapper::toDomain);
    }

    @Override
    public List<SalaDeAula> buscarPorMembro(UsuarioId usuarioId) {
        return jpaRepository.findByMembros_UsuarioId(usuarioId.valor()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<SalaDeAula> buscarPorCriador(UsuarioId criadorId) {
        return jpaRepository.findByCriadorId(criadorId.valor()).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
