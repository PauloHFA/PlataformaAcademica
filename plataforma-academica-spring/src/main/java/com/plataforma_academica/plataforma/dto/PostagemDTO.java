package com.plataforma_academica.plataforma.dto;

import lombok.Data;

/**
 * DTO de transferência para Postagens no feed social.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Postagem
 * @see PostagemMapper
 */
@Data
public class PostagemDTO {

    private Long id;
    private String titulo;
    private String conteudo;

    private Long autorId;
    private Long plataformaId;
    private String imagemUrl;
}