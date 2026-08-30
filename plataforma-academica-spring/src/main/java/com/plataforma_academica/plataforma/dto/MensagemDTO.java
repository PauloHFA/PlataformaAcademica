package com.plataforma_academica.plataforma.dto;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de transferência para Mensagens Diretas.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Mensagem
 */
public class MensagemDTO {
    @NotNull
    private UUID remetenteId;

    @NotNull
    private UUID destinatarioId;

    @NotBlank
    private String conteudo;

    public MensagemDTO() {
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
}
