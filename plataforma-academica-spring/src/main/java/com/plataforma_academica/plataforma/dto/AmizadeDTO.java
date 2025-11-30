package com.plataforma_academica.plataforma.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

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
