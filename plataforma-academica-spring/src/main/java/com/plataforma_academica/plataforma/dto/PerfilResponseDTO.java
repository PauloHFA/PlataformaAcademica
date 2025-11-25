package com.plataforma_academica.plataforma.dto;

import lombok.Data;

@Data
public class PerfilResponseDTO {

    private Long id;
    private String bio;
    private String fotoPerfil;
    private String curso;

    // Informações do usuário dono do perfil
    private Long usuarioId;
    private String usuarioNome;

    public void setUsuarioEmail(String email) {
    }
}
