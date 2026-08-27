package com.plataforma_academica.plataforma.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de transferência para solicitações de Amizade.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Amizade
 * @see AmizadeMapper
 */
@Data
public class AmizadeDTO {

    private Long id;

    @NotNull(message = "ID do solicitante é obrigatório")
    private Long solicitanteId;

    @NotNull(message = "ID do destinatário é obrigatório")
    private Long destinatarioId;

    private String status;

    private LocalDateTime criadoEm;
}
