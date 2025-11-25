package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AtividadeResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private String tipoDocumentoSubmissao;
    private LocalDate dataEntrega;
    private Double pontos;

    // Informações da sala
    private Long salaId;
    private String salaNome;     // opcional, mas recomendado

    // Informações do autor
    private Long autorId;
    private String autorNome;    // opcional e útil para o front
}
