package com.plataforma_academica.plataforma.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de transferência para criação/atualização de Atividades.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see Atividade
 * @see AtividadeMapper
 */
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
