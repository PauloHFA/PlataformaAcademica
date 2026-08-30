package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;

/**
 * DTO de transferÃªncia para criaÃ§Ã£o/atualizaÃ§Ã£o de Perfis.
 * 
 * Camada: Presentation / DTO (Identity Context)
 * 
 * @see Perfil
 * @see PerfilMapper
 */
@Data
public class PerfilDTO {
    private UUID id;
    private String nome;
    private String sobrenome;
    private String email;
    private String instituicaoEnsino;
    private String cep;
    private String pais;
    private String cidade;
    private String site;
    private String telefone;
    private String dataNascimento;
    private String descricao;
    private String bio;
    private String fotoPerfil;
    private String curso;
    private String senha; // opcional para ediÃ§Ã£o
    private UUID usuarioId; // mesmo que id, para compatibilidade
}

