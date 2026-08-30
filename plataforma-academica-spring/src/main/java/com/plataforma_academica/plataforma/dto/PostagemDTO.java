package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;

/**
 * DTO de transferÃªncia para Postagens no feed social.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Postagem
 * @see PostagemMapper
 */
@Data
public class PostagemDTO {

    private UUID id;
    private String titulo;
    private String conteudo;

    private UUID autorId;
    private UUID plataformaId;
    private String imagemUrl;
}
