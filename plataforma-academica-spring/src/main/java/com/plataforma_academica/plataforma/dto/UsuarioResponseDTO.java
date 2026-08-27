package com.plataforma_academica.plataforma.dto;

import lombok.Data;

/**
 * DTO de resposta para dados de Usuário.
 * 
 * Camada: Presentation / DTO (Identity Context)
 * 
 * @see Usuario
 * @see UsuarioMapper
 */
@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;

    private String avatarUrl; // para exibição
    private String avatarBase64; // opcional, só se você realmente precisar enviar
}
