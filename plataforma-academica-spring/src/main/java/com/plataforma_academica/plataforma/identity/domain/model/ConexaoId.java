package com.plataforma_academica.plataforma.identity.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa a identidade única de uma Conexão de Amizade.
 * 
 * Este identificador garante a unicidade de cada relacionamento de amizade
 * estabelecido ou solicitado no ecossistema.
 * 
 * Padrão aplicado: Value Object (Imutável).
 */
public final class ConexaoId {
    private final UUID valor;

    /**
     * Construtor privado para garantir a imutabilidade e validação de integridade.
     * 
     * @param valor UUID que representa o identificador.
     * @throws IllegalArgumentException se o valor for nulo.
     */
    private ConexaoId(UUID valor) {
        if (valor == null) {
            throw new IllegalArgumentException("ConexaoId não pode ser nulo");
        }
        this.valor = valor;
    }

    /**
     * Factory Method para gerar um novo identificador único aleatório.
     * 
     * @return Nova instância de ConexaoId.
     */
    public static ConexaoId novo() {
        return new ConexaoId(UUID.randomUUID());
    }

    /**
     * Factory Method para instanciar a partir de um UUID existente.
     * 
     * @param valor UUID existente.
     * @return Instância de ConexaoId.
     */
    public static ConexaoId de(UUID valor) {
        return new ConexaoId(valor);
    }

    /**
     * Factory Method para instanciar a partir de uma representação em String do
     * UUID.
     * 
     * @param valor String representando o UUID.
     * @return Instância de ConexaoId.
     */
    public static ConexaoId de(String valor) {
        return new ConexaoId(UUID.fromString(valor));
    }

    /**
     * Retorna o valor primitivo do identificador.
     * 
     * @return UUID do identificador.
     */
    public UUID valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ConexaoId that = (ConexaoId) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}