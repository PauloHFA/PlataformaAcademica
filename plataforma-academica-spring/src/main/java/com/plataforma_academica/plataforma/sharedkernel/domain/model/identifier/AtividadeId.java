package com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier;

import java.util.UUID;

/**
 * Value Object imutável de Identidade Única da Atividade Acadêmica.
 * 
 * Este identificador garante a unicidade de cada atividade criada no contexto
 * acadêmico.
 * Implementa a interface {@link Identificador} para padronização de IDs no
 * sistema.
 * 
 * Padrão aplicado: Value Object (Imutável).
 */
public record AtividadeId(UUID valor) implements Identificador {

    /**
     * Construtor compacto para validação de integridade.
     * 
     * @throws IllegalArgumentException se o valor for nulo.
     */
    public AtividadeId {
        if (valor == null) {
            throw new IllegalArgumentException("O valor do AtividadeId não pode ser nulo.");
        }
    }

    /**
     * Factory Method para gerar um novo identificador único aleatório.
     * 
     * @return Nova instância de AtividadeId.
     */
    public static AtividadeId novo() {
        return new AtividadeId(UUID.randomUUID());
    }

    /**
     * Factory Method para instanciar a partir de um UUID existente.
     * 
     * @param valor UUID existente.
     * @return Instância de AtividadeId.
     */
    public static AtividadeId de(UUID valor) {
        return new AtividadeId(valor);
    }

    /**
     * Factory Method para instanciar a partir de uma representação em String do
     * UUID.
     * 
     * @param uuidString String representando o UUID.
     * @return Instância de AtividadeId.
     * @throws IllegalArgumentException se a string for vazia ou inválida.
     */
    public static AtividadeId de(String uuidString) {
        if (uuidString == null || uuidString.isBlank()) {
            throw new IllegalArgumentException("A string UUID do AtividadeId não pode ser vazia.");
        }
        return new AtividadeId(UUID.fromString(uuidString));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
