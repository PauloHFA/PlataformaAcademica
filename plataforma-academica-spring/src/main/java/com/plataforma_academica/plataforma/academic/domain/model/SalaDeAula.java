package com.plataforma_academica.plataforma.academic.domain.model;

import java.util.List;
import java.util.UUID;

/**
 * Agregado Raiz que representa uma Sala de Aula Virtual.
 * No DDD, o domínio deve ser puro e não depender de anotações de persistência
 * (JPA).
 */
public class SalaDeAula {
    private final UUID id;
    private String nome;
    private final String codigoSala;
    private final UUID criadorId;
    private List<UUID> membrosIds;

    public SalaDeAula(UUID id, String nome, String codigoSala, UUID criadorId) {
        this.id = id;
        this.nome = nome;
        this.codigoSala = codigoSala;
        this.criadorId = criadorId;
    }

    // Regras de negócio podem ser adicionadas aqui
    public void alterarNome(String novoNome) {
        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException("Nome da sala não pode ser vazio");
        }
        this.nome = novoNome;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigoSala() {
        return codigoSala;
    }

    public UUID getCriadorId() {
        return criadorId;
    }

    public List<UUID> getMembrosIds() {
        return membrosIds;
    }

    public void setMembrosIds(List<UUID> membrosIds) {
        this.membrosIds = membrosIds;
    }
}
