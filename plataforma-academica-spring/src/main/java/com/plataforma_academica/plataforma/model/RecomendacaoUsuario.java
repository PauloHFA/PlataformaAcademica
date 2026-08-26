package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma recomendação de usuário baseada em
 * similaridade.
 * 
 * Camada: Persistence / Domain Entity (Social / Identity Context)
 * Contexto de Negócio: Sistema de recomendação que sugere conexões entre
 * usuários
 * com base em interações, interesses e pontuação de similaridade.
 * 
 * Trade-off: scoreSimilaridade é calculado por algoritmo externo (não
 * persistido
 * como regra de negócio aqui), permitindo flexibilidade de modelo.
 * 
 * @see Usuario
 * @see InteracaoUsuario
 * @see docs/domain/social_context.md
 * @see REQ-042 (Sistema de Recomendação por Similaridade)
 */
@Entity
@Table(name = "recomendacao_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacaoUsuario {

    /** Identificador único da recomendação. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuário alvo da recomendação. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Usuário recomendado (similar ao alvo). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_recomendado_id", nullable = false)
    private Usuario usuarioRecomendado;

    /** Pontuação de similaridade entre os usuários (0.0 a 1.0). */
    @Column(name = "score_similaridade", nullable = false)
    private Double scoreSimilaridade;

    /** Motivo textual da recomendação (ex: "interesses em comum"). */
    @Column(name = "motivo_recomendacao")
    private String motivoRecomendacao;

    /**
     * Tipo de recomendação (ESTUDO_GRUPO, AMIZADE, MENTORIA, COLABORACAO_PROJETO).
     */
    @Column(name = "tipo_recomendacao")
    @Enumerated(EnumType.STRING)
    private TipoRecomendacao tipoRecomendacao;

    /** Data e hora de criação da recomendação. */
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    /**
     * Flag indicando se a recomendação está ativa (true) ou foi descartada (false).
     */
    @Column(name = "ativo")
    private Boolean ativo = true;

    public enum TipoRecomendacao {
        ESTUDO_GRUPO,
        AMIZADE,
        MENTORIA,
        COLABORACAO_PROJETO
    }
}