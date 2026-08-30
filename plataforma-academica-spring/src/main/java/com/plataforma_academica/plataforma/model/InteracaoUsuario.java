package com.plataforma_academica.plataforma.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidade JPA que registra interações de usuários para fins analíticos e de
 * recomendação.
 * 
 * Camada: Persistence / Analytics Entity
 * Usada pelo motor de recomendação para pontuar afinidades baseadas em cliques,
 * curtidas, comentários, etc.
 * 
 * @see docs/architecture/recommendation-engine.md
 */
@Entity
@Table(name = "interacao_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteracaoUsuario {

    /** Identificador único da interação. */
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    /** Usuário que realizou a ação. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Tipo da interação realizada. */
    @Column(name = "tipo_interacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoInteracao tipoInteracao;

    /** Tipo da entidade alvo (POSTAGEM, ATIVIDADE, COMUNIDADE, etc.). */
    @Column(name = "entidade_tipo")
    private String entidadeTipo; // "POSTAGEM", "ATIVIDADE", "COMUNIDADE", etc.

    /** ID da entidade alvo da interação. */
    @Column(name = "entidade_id")
    private UUID entidadeId;

    /** Peso numérico atribuído à interação para cálculos de relevância. */
    @Column(name = "peso_interacao", nullable = false)
    private Double pesoInteracao = 1.0;

    /** Timestamp da ocorrência da interação. */
    @Column(name = "data_interacao")
    private LocalDateTime dataInteracao = LocalDateTime.now();

    /** Tags associadas em formato JSON ou string delimitada. */
    @Column(name = "tags")
    private String tags; // JSON array de tags relacionadas

    /**
     * Enumeração dos tipos de interações suportados pelo sistema de análise.
     */
    public enum TipoInteracao {
        VISUALIZACAO,
        CURTIDA,
        COMENTARIO,
        COMPARTILHAMENTO,
        PARTICIPACAO_ATIVIDADE,
        ENTRADA_COMUNIDADE,
        ENVIO_SOLICITACAO_AMIZADE,
        ACEITACAO_AMIZADE
    }


    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}