package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Data
@Entity
@Table(name = "usuario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "atividadesCriadas", "postagens", "comentarios", "submissões"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    
    // Manter a senha fora das respostas, mas permitir desserialização no login/cadastro
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Lob
    @JsonIgnore // Ignorar avatar na serialização (pode ser muito grande)
    private byte[] avatar;

    // Postagens criadas pelo usuário
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Postagem> postagens;

    // Comentários feitos pelo usuário
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;

    // Plataformas em que o usuário está vinculado
    @ManyToOne
    @JoinColumn(name = "plataforma_id")
    private Plataforma plataforma;

    // Atividades criadas pelo usuário
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atividade> atividadesCriadas;

    // Submissões feitas pelo usuário
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmissaoAtividade> submissões;
}
