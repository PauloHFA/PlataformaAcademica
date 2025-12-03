package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AtividadeResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private String tipoDocumentoSubmissao;
    private String dataEntrega;
    private Double pontos;
    private String documentoUrl;

    // Informações da sala
    private Long salaId;
    private String salaNome;

    // Informações do autor
    private Long autorId;
    private String autorNome;
}
