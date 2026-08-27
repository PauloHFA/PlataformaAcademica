package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de resposta para Submissões de Atividades.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see SubmissaoAtividade
 * @see SubmissaoAtividadeMapper
 */
@Data
public class SubmissaoAtividadeResponseDTO {

    private Long id;

    private Long atividadeId;
    private String atividadeTitulo; // opcional, facilita exibição

    private Long alunoId;
    private String alunoNome; // opcional, evita requisição adicional

    private String urlDocumento;
    private String descricao;

    private LocalDateTime dataSubmissao;

    private Double nota;
    private String feedback;
}
