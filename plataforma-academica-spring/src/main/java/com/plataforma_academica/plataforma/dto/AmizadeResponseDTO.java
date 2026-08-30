package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

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

    private UUID id;

    // Dados do solicitante
    private UUID solicitanteId;
    private String solicitanteNome;

    // Dados do destinatÃ¡rio
    private UUID destinatarioId;
    private String destinatarioNome;

    private String status;

    private LocalDateTime criadoEm;
}

