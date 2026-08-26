package com.plataforma_academica.plataforma.dto;

import lombok.Data;

/**
 * DTO de transferência para criação/atualização de Usuários.
 * 
 * Camada: Presentation / DTO (Identity Context)
 * 
 * @see Usuario
 * @see UsuarioMapper
 */
@Data
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String dataNascimento;
    private String telefone;
    private String descricao;
    private String instituicaoEnsino;
    private String cep;
    private String pais;
    private String cidade;
    private String site;
    private String avatarBase64;
}
