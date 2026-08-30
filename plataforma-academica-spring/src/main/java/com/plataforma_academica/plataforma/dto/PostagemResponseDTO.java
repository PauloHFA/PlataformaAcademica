package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

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

    private UUID id;
    private String titulo;
    private String conteudo;

    private UUID autorId;
    private String autorNome;

    private UUID plataformaId;
    private String plataformaNome;

    private Integer curtidas;
    private String imagemUrl;
}

