package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

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
