package com.plataforma_academica.plataforma.dto;

import lombok.Data;

@Data
public class PerfilDTO {
    private Long id;
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
    private String senha; // opcional para edição
    private Long usuarioId; // mesmo que id, para compatibilidade
}
