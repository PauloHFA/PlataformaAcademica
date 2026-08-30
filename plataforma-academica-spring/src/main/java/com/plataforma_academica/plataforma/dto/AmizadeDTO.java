package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de transferÃªncia para solicitaÃ§Ãµes de Amizade.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Amizade
 * @see AmizadeMapper
 */
@Data
public class AmizadeDTO {

    private UUID id;

    @NotNull(message = "ID do solicitante Ã© obrigatÃ³rio")
    private UUID solicitanteId;

    @NotNull(message = "ID do destinatÃ¡rio Ã© obrigatÃ³rio")
    private UUID destinatarioId;

    private String status;

    private LocalDateTime criadoEm;
}

