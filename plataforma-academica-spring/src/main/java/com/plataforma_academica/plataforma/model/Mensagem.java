package com.plataforma_academica.plataforma.model;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID do usuário remetente. */
    @Column(nullable = false)
    private Long remetenteId;

    /** ID do usuário destinatário. */
    @Column(nullable = false)
    private Long destinatarioId;

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

    public Mensagem(Long remetenteId, Long destinatarioId, String conteudo) {
        this.remetenteId = remetenteId;
        this.destinatarioId = destinatarioId;
        this.conteudo = conteudo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRemetenteId() {
        return remetenteId;
    }

    public void setRemetenteId(Long remetenteId) {
        this.remetenteId = remetenteId;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(Long destinatarioId) {
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
}
