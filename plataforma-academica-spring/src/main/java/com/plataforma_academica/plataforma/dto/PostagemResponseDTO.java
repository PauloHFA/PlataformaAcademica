package com.plataforma_academica.plataforma.dto;

import lombok.Data;

/**
 * DTO de resposta para Postagens.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Postagem
 * @see PostagemMapper
 */
@Data
public class PostagemResponseDTO {

    private Long id;
    private String titulo;
    private String conteudo;

    private Long autorId;
    private String autorNome;

    private Long plataformaId;
    private String plataformaNome;

    private Integer curtidas;
    private String imagemUrl;
}
