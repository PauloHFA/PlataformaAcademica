package com.plataforma_academica.plataforma.identity.infrastructure.persistence;

import com.plataforma_academica.plataforma.identity.domain.model.Email;
import com.plataforma_academica.plataforma.identity.domain.model.Papel;
import com.plataforma_academica.plataforma.identity.domain.model.PerfilUsuario;
import com.plataforma_academica.plataforma.identity.domain.model.SaldoGamificacao;
import com.plataforma_academica.plataforma.identity.domain.model.SenhaHash;
import com.plataforma_academica.plataforma.identity.domain.model.Usuario;
import com.plataforma_academica.plataforma.identity.domain.model.UsuarioId;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Mapper entre Entidade JPA e Modelo de Domínio.
 * Mantém o isolamento entre camadas.
 */
@Component
public class UsuarioEntityMapper {

    public UsuarioEntity toEntity(Usuario domain) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(domain.id().valor());
        entity.setNome(domain.nome());
        entity.setEmail(domain.email().endereco());
        entity.setSenhaHash(domain.senhaHash().hash());

        PerfilUsuario perfil = domain.perfil();
        entity.setFotoUrl(perfil.fotoUrl());
        entity.setBiografia(perfil.biografia());
        entity.setInstituicao(perfil.instituicao());
        entity.setCep(perfil.cep());
        entity.setSite(perfil.site());

        entity.setPapeis(new HashSet<>(domain.papeis()));

        SaldoGamificacao saldo = domain.saldo();
        entity.setPontos(saldo.pontos());
        entity.setMoedas(saldo.moedas());
        entity.setNivel(saldo.nivel());

        entity.setDataCadastro(domain.dataCadastro());
        entity.setUltimoLogin(domain.ultimoLogin());

        return entity;
    }

    public Usuario toDomain(UsuarioEntity entity) {
        UsuarioId id = UsuarioId.de(entity.getId());
        Email email = Email.de(entity.getEmail());
        SenhaHash senhaHash = SenhaHash.de(entity.getSenhaHash());

        PerfilUsuario perfil = PerfilUsuario.criar(
                entity.getFotoUrl(),
                entity.getBiografia(),
                entity.getInstituicao(),
                entity.getCep(),
                entity.getSite());

        Set<Papel> papeis = new HashSet<>(entity.getPapeis());

        SaldoGamificacao saldo = SaldoGamificacao.de(
                entity.getPontos(),
                entity.getMoedas(),
                entity.getNivel());

        // Usar reflexão para reconstruir o agregado completo
        return Usuario.reconstruir(
                id,
                email,
                senhaHash,
                entity.getNome(),
                perfil,
                papeis,
                saldo,
                entity.getDataCadastro(),
                entity.getUltimoLogin());
    }
}