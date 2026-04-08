package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Data
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PADRAO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "atividadesCriadas", "postagens", "comentarios", "submissões"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    
    private String sobrenome;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String senha;
    
    private java.time.LocalDate dataNascimento;
    
    private String telefone;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    private String instituicaoEnsino;
    
    private String cep;
    
    private String pais;
    
    private String cidade;
    
    private String site;

    @Lob
    @JsonIgnore
    private byte[] avatar;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String fotoPerfil;

    private Double pontos = 0.0;
    private Double moedas = 0.0;
    private String nivel = "Bronze";

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

    @Version
    private Long version;
}
