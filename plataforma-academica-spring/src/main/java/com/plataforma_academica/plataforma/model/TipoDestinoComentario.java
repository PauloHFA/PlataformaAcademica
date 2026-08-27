/**
 * Enumeração dos tipos de destino possíveis para um comentário.
 * 
 * Camada: Persistence / Domain Enum
 * Contexto de Negócio: Define a entidade raiz à qual o comentário está vinculado,
 * permitindo comentários generalizados (postagem, atividade, sala de aula).
 * 
 * @see Comentario
 * @see docs/domain/social_context.md
 */
package com.plataforma_academica.plataforma.model;

public enum TipoDestinoComentario {
    POSTAGEM,
    ATIVIDADE,
    SALADEAULA,
    ATIVIDADES_GERAIS
}
