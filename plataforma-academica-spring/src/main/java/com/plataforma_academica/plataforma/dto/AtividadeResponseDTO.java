package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO de resposta para Atividades.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see Atividade
 * @see AtividadeMapper
 */
@Data
public class AtividadeResponseDTO {

    private UUID id;
    private String titulo;
    private String descricao;
    private String tipoDocumentoSubmissao;
    private String dataEntrega;
    private Double pontos;
    private String documentoUrl;

    // InformaÃ§Ãµes da sala
    private UUID salaId;
    private String salaNome;

    // InformaÃ§Ãµes do autor
    private UUID autorId;
    private String autorNome;
}

