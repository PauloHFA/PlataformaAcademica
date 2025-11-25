package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComunidadeDTO {

    private Long id;
    private String nome;
    private String descricao;

    private LocalDateTime criadoEm;

    private Long donoId;
}