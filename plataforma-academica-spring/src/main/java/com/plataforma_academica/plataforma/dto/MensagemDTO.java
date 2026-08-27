package com.plataforma_academica.plataforma.dto;

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
    private Long remetenteId;

    @NotNull
    private Long destinatarioId;

    @NotBlank
    private String conteudo;

    public MensagemDTO() {
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
}
