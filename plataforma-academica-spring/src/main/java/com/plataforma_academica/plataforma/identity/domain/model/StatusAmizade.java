package com.plataforma_academica.plataforma.identity.domain.model;

/**
 * Enumeração dos possíveis estados de uma conexão de amizade (Máquina de
 * Estados).
 * 
 * Define o ciclo de vida de uma solicitação de amizade:
 * 
 * <pre>
 * PENDENTE  -> ACEITO    (destinatário aceita)
 * PENDENTE  -> RECUSADO  (destinatário recusa)
 * ACEITO    -> BLOQUEADO (qualquer participante bloqueia)
 * RECUSADO  -> BLOQUEADO (qualquer participante bloqueia)
 * </pre>
 * 
 * Transições inválidas lançam {@link IllegalStateException} no agregado
 * {@link ConexaoAmizade}.
 */
public enum StatusAmizade {
    /**
     * Solicitação enviada, aguardando resposta do destinatário.
     */
    PENDENTE,

    /**
     * Solicitação aceita - usuários são amigos.
     */
    ACEITO,

    /**
     * Solicitação recusada pelo destinatário.
     */
    RECUSADO,

    /**
     * Conexão bloqueada por um dos participantes.
     * Estado terminal - não permite transições para outros estados.
     */
    BLOQUEADO
}