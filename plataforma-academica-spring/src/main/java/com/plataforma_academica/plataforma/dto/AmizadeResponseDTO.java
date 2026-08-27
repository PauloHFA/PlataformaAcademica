package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de resposta para Amizades.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Amizade
 * @see AmizadeMapper
 */
@Data
public class AmizadeResponseDTO {

    private Long id;

    // Dados do solicitante
    private Long solicitanteId;
    private String solicitanteNome;

    // Dados do destinatário
    private Long destinatarioId;
    private String destinatarioNome;

    private String status;

    private LocalDateTime criadoEm;
}
