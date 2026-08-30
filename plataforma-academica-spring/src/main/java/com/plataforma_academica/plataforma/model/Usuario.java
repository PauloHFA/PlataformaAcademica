package com.plataforma_academica.plataforma.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Entidade JPA que representa o Usuário no modelo legado de persistência.
 * 
 * Camada: Persistence / JPA Entity
 * Utiliza herança do tipo JOINED (tabelas filhas como Professor, Admin, etc.).
 * Contém dados de autenticação, contato e perfil básico.
 */
@Data
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PADRAO")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "atividadesCriadas", "postagens", "comentarios",
        "submissões" })
public class Usuario {

    /** Identificador único do usuário. */
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    /** Primeiro nome do usuário. */
    @Column(nullable = false)
    private String nome;

    /** Sobrenome do usuário. */
    private String sobrenome;

    /** E-mail de cadastro (único). */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Senha criptografada (acesso exclusivo para escrita por motivos de segurança).
     */
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String senha;

    /** Data de nascimento. */
    private java.time.LocalDate dataNascimento;

    /** Número de telefone. */
    private String telefone;

    /** Descrição ou biografia resumida. */
    @Column(columnDefinition = "TEXT")
    private String descricao;

    /** Instituição de ensino associada. */
    private String instituicaoEnsino;

    /** CEP de localização. */
    private String cep;

    /** País de residência. */
    private String pais;

    /** Cidade de residência. */
    private String cidade;

    /** Website pessoal ou portfólio. */
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
    private UUID version;


    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
