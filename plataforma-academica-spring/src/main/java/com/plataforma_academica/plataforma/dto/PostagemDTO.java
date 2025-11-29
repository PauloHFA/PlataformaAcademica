package com.plataforma_academica.plataforma.dto;

import lombok.Data;

@Data
public class PostagemDTO {

    private Long id;
    private String titulo;
    private String conteudo;

    private Long autorId;
    private Long plataformaId;
    private String imagemUrl;
}