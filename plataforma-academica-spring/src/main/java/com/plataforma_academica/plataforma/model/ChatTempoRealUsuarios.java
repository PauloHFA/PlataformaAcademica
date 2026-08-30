/**
 * Entidade de controle para chat em tempo real entre usuários.
 *
 * Camada: Persistence / Domain Entity (Social Context)
 * Contexto de Negócio: Gerencia sessões de chat, participantes e mensagens
 * em tempo real (WebSocket / Socket.IO).
 *
 * @see Mensagem
 * @see Usuario
 * @see docs/domain/social_context.md
 */
package com.plataforma_academica.plataforma.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chat_tempo_real_usuarios")
public class ChatTempoRealUsuarios {

    /** Identificador único do registro. */
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    /** Usuário conectado à sala de chat. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Identificador lógico da sala de chat. */
    @Column(nullable = false)
    private String salaChat;

    /** Data e hora em que o usuário se conectou. */
    private LocalDateTime conectadoEm = LocalDateTime.now();

    /** Indicador de sessão ativa. */
    private Boolean ativo = true;


    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
