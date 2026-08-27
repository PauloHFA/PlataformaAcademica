package com.plataforma_academica.plataforma.dto;

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

    private Long id;
    private String titulo;
    private String conteudo;

    // Informações resumidas do autor
    private Long autorId;
    private String autorNome;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
