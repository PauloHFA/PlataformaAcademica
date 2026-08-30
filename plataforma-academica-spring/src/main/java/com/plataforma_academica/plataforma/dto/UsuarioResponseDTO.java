package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;

/**
 * DTO de resposta para dados de UsuÃ¡rio.
 * 
 * Camada: Presentation / DTO (Identity Context)
 * 
 * @see Usuario
 * @see UsuarioMapper
 */
@Data
public class UsuarioResponseDTO {

    private UUID id;
    private String nome;
    private String email;

    private String avatarUrl; // para exibiÃ§Ã£o
    private String avatarBase64; // opcional, sÃ³ se vocÃª realmente precisar enviar
}

