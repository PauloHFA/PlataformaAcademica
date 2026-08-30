package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de transferÃªncia para SubmissÃµes de Atividades.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see SubmissaoAtividade
 * @see SubmissaoAtividadeMapper
 */
@Data
public class SubmissaoAtividadeDTO {

    private UUID id;

    private UUID atividadeId;
    private UUID alunoId;

    private String urlDocumento;

    private LocalDateTime dataSubmissao;

    private Double nota;
    private String feedback;
}

