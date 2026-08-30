package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de resposta para Artigos.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see Artigo
 * @see ArtigoMapper
 */
@Data
public class ArtigoResponseDTO {

    private UUID id;
    private String titulo;
    private String conteudo;

    // InformaÃ§Ãµes resumidas do autor
    private UUID autorId;
    private String autorNome;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}

