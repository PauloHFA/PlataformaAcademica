package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import com.plataforma_academica.plataforma.academic.domain.model.SalaDeAula;
import com.plataforma_academica.plataforma.academic.domain.repository.SalaDeAulaRepository;
import org.springframework.stereotype.Component;
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
    public Optional<SalaDeAula> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<SalaDeAula> buscarPorCodigo(String codigo) {
        return jpaRepository.findByCodigoSala(codigo).map(mapper::toDomain);
    }
}
