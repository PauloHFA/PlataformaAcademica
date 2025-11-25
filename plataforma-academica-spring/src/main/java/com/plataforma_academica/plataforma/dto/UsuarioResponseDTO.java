package com.plataforma_academica.plataforma.dto;

import lombok.Data;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;

    private String avatarUrl;      // para exibição
    private String avatarBase64;   // opcional, só se você realmente precisar enviar
}
