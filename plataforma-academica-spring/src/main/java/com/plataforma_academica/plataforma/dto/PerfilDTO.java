package com.plataforma_academica.plataforma.dto;

import lombok.Data;

@Data
public class PerfilDTO {
    private Long id;
    private String bio;
    private String fotoPerfil;
    private String curso;
    private Long usuarioId; // evita expor a entidade Usuario
}
