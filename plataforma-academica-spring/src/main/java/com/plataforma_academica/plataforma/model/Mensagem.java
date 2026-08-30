package com.plataforma_academica.plataforma.model;

import java.util.UUID;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma mensagem direta entre usuários.
 *
 * Camada: Persistence / Domain Entity (Social Context)
 * Contexto de Negócio: Comunicação privada (chat) entre remetente e
 * destinatário,
 * com controle de leitura e conteúdo textual.
 *
 * @see docs/domain/social_context.md
 * @see REQ-035 (Mensagens Diretas)
 */
@Entity
@Table(name = "mensagens")
public class Mensagem {
    /** Identificador único da mensagem. */
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    /** ID do usuário remetente. */
    @Column(nullable = false)
    private UUID remetenteId;

    /** ID do usuário destinatário. */
    @Column(nullable = false)
    private UUID destinatarioId;

    /** Conteúdo textual da mensagem (suporta textos longos). */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    /** Data e hora de criação da mensagem. */
    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    /** Indicador de leitura (false = não lida pelo destinatário). */
    @Column(nullable = false)
    private Boolean lida = false;

    public Mensagem() {
    }

    public Mensagem(UUID remetenteId, UUID destinatarioId, String conteudo) {
        this.remetenteId = remetenteId;
        this.destinatarioId = destinatarioId;
        this.conteudo = conteudo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRemetenteId() {
        return remetenteId;
    }

    public void setRemetenteId(UUID remetenteId) {
        this.remetenteId = remetenteId;
    }

    public UUID getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(UUID destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }


    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
