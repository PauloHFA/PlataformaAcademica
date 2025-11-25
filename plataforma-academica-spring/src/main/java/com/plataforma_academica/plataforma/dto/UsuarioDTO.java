package com.plataforma_academica.plataforma.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String avatarBase64; // opcional
}
