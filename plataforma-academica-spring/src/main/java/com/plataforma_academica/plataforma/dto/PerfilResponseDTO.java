package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;

/**
 * DTO de resposta para Perfis.
 * 
 * Camada: Presentation / DTO (Identity Context)
 * 
 * @see Perfil
 * @see PerfilMapper
 */
@Data
public class PerfilResponseDTO {

    private UUID id;
    private String bio;
    private String fotoPerfil;
    private String curso;

    // InformaÃ§Ãµes do usuÃ¡rio dono do perfil
    private UUID usuarioId;
    private String usuarioNome;

    public void setUsuarioEmail(String email) {
    }
}

