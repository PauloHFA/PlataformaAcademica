package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma notificação dirigida a um usuário.
 *
 * Camada: Persistence / JPA Entity (Identity Context)
 * Contexto de Negócio: Alertas sobre eventos relevantes (ATIVIDADE_CRIADA,
 * NOTA_ATRIBUIDA, SOLICITACAO_AMIZADE, etc.) rastreáveis por tipo e referência.
 * Padrões aplicados: Domain Event (modelo), Repository Pattern.
 *
 * @see docs/domain/identity_context.md
 * @see REQ-005 (Sistema de Notificações)
 */
@Entity
@Data
@Table(name = "notificacao")
public class Notificacao {
    /** Identificador único da notificação. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuário destinatário da notificação. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Mensagem textual da notificação. */
    private String mensagem;

    /** Tipo de evento (ex: ATIVIDADE_CRIADA, NOTA_ATRIBUIDA). */
    private String tipo;

    /** ID da entidade relacionada (atividade, sala, etc.). */
    private Long referenciaId;

    /** Indicador de leitura (false = não lida). */
    private Boolean lida = false;

    /** Data e hora de criação da notificação. */
    private LocalDateTime dataCriacao = LocalDateTime.now();
}
