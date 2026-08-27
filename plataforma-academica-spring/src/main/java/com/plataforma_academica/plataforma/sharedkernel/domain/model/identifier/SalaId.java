package com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier;

import java.util.UUID;

/**
 * Value Object imutável de Identidade Única da Sala de Aula Virtual.
 * 
 * Este identificador garante a unicidade de cada sala de aula criada no
 * contexto acadêmico.
 * Implementa a interface {@link Identificador} para padronização de IDs no
 * sistema.
 * 
 * Padrão aplicado: Value Object (Imutável).
 */
public record SalaId(UUID valor) implements Identificador {

    /**
     * Construtor compacto para validação de integridade.
     * 
     * @throws IllegalArgumentException se o valor for nulo.
     */
    public SalaId {
        if (valor == null) {
            throw new IllegalArgumentException("O valor do SalaId não pode ser nulo.");
        }
    }

    /**
     * Factory Method para gerar um novo identificador único aleatório.
     * 
     * @return Nova instância de SalaId.
     */
    public static SalaId novo() {
        return new SalaId(UUID.randomUUID());
    }

    /**
     * Factory Method para instanciar a partir de um UUID existente.
     * 
     * @param valor UUID existente.
     * @return Instância de SalaId.
     */
    public static SalaId de(UUID valor) {
        return new SalaId(valor);
    }

    /**
     * Factory Method para instanciar a partir de uma representação em String do
     * UUID.
     * 
     * @param uuidString String representando o UUID.
     * @return Instância de SalaId.
     * @throws IllegalArgumentException se a string for vazia ou inválida.
     */
    public static SalaId de(String uuidString) {
        if (uuidString == null || uuidString.isBlank()) {
            throw new IllegalArgumentException("A string UUID do SalaId não pode ser vazia.");
        }
        return new SalaId(UUID.fromString(uuidString));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
