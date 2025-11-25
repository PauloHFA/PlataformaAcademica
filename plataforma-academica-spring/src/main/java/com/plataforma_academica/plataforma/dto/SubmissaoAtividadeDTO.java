package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

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
