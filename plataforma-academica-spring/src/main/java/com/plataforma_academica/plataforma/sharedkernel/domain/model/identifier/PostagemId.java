package com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier;

import java.util.UUID;

/**
 * Value Object imutável de Identidade Única da Postagem Social.
 * 
 * Este identificador garante a unicidade de cada postagem criada no contexto
 * social da plataforma.
 * Implementa a interface {@link Identificador} para padronização de IDs no
 * sistema.
 * 
 * Padrão aplicado: Value Object (Imutável).
 */
public record PostagemId(UUID valor) implements Identificador {

    /**
     * Construtor compacto para validação de integridade.
     * 
     * @throws IllegalArgumentException se o valor for nulo.
     */
    public PostagemId {
        if (valor == null) {
            throw new IllegalArgumentException("O valor do PostagemId não pode ser nulo.");
        }
    }

    /**
     * Factory Method para gerar um novo identificador único aleatório.
     * 
     * @return Nova instância de PostagemId.
     */
    public static PostagemId novo() {
        return new PostagemId(UUID.randomUUID());
    }

    /**
     * Factory Method para instanciar a partir de um UUID existente.
     * 
     * @param valor UUID existente.
     * @return Instância de PostagemId.
     */
    public static PostagemId de(UUID valor) {
        return new PostagemId(valor);
    }

    /**
     * Factory Method para instanciar a partir de uma representação em String do
     * UUID.
     * 
     * @param uuidString String representando o UUID.
     * @return Instância de PostagemId.
     * @throws IllegalArgumentException se a string for vazia ou inválida.
     */
    public static PostagemId de(String uuidString) {
        if (uuidString == null || uuidString.isBlank()) {
            throw new IllegalArgumentException("A string UUID do PostagemId não pode ser vazia.");
        }
        return new PostagemId(UUID.fromString(uuidString));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
