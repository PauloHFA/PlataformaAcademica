package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de transferência para Submissões de Atividades.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see SubmissaoAtividade
 * @see SubmissaoAtividadeMapper
 */
@Data
public class SubmissaoAtividadeDTO {

    private Long id;

    private Long atividadeId;
    private Long alunoId;

    private String urlDocumento;

    private LocalDateTime dataSubmissao;

    private Double nota;
    private String feedback;
}
