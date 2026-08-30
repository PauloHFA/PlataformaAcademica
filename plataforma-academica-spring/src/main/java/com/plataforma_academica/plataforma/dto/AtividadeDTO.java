package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de transferÃªncia para criaÃ§Ã£o/atualizaÃ§Ã£o de Atividades.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see Atividade
 * @see AtividadeMapper
 */
@Data
public class AtividadeDTO {

    private UUID id;
    private String titulo;
    private String descricao;
    private String tipoDocumentoSubmissao;
    private String dataEntrega;
    private Double pontos;

    private UUID salaId;
    private UUID autorId;
}

