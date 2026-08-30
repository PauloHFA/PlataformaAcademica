package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de resposta para SubmissÃµes de Atividades.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see SubmissaoAtividade
 * @see SubmissaoAtividadeMapper
 */
@Data
public class SubmissaoAtividadeResponseDTO {

    private UUID id;

    private UUID atividadeId;
    private String atividadeTitulo; // opcional, facilita exibiÃ§Ã£o

    private UUID alunoId;
    private String alunoNome; // opcional, evita requisiÃ§Ã£o adicional

    private String urlDocumento;
    private String descricao;

    private LocalDateTime dataSubmissao;

    private Double nota;
    private String feedback;
}

