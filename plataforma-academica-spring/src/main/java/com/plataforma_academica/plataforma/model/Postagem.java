package com.plataforma_academica.plataforma.model;

import java.util.UUID;

import jakarta.persistence.*;
import com.plataforma_academica.plataforma.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade JPA que representa uma postagem no feed social da plataforma.
 *
 * Camada: Persistence / Domain Entity (Social Context)
 * Contexto de Negócio: Publicações realizadas por usuários em uma plataforma
 * acadêmica, com contagem de curtidas e imagem opcional.
 * Padrões aplicados: Aggregate Root, Repository Pattern.
 *
 * @see docs/domain/social_context.md
 * @see REQ-025 (Publicação no Feed Social)
 */
@Data
@Getter
@Setter
@Entity
@Table(name = "postagem")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Postagem {

    /** Identificador único da postagem. */
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    /** Título da postagem (exposto no feed). */
    private String titulo;

    /** Conteúdo textual da postagem. */
    private String conteudo;

    /** Autor da postagem. */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario autor;

    @ManyToOne
    @JoinColumn(name = "plataforma_id")
    private Plataforma plataforma;

    /** Contador de curtidas (denormalizado para performance de feed). */
    private Integer curtidas = 0;

    /** URL da imagem anexada (opcional). */
    private String imagemUrl;



    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}