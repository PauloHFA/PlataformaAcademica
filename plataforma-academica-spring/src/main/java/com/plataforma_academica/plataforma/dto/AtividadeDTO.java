package com.plataforma_academica.plataforma.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AtividadeDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private String tipoDocumentoSubmissao;
    private String dataEntrega;
    private Double pontos;

    private Long salaId;
    private Long autorId;
}
