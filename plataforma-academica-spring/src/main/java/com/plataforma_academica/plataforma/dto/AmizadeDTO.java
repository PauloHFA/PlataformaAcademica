package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AmizadeDTO {

    private Long id;

    private Long solicitanteId;
    private Long destinatarioId;

    private String status;

    private LocalDateTime criadoEm;
}
