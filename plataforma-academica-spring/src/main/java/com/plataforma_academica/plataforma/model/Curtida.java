package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidade JPA que representa uma curtida (like) de um usuário em uma postagem.
 *
 * Camada: Persistence / Domain Entity (Social Context)
 * Invariante: combinação (usuario, postagem) é única (restrição de banco).
 *
 * @see REQ-030 (Sistema de Curtidas)
 */
@Data
@Entity
@Table(name = "curtida", uniqueConstraints = @UniqueConstraint(columnNames = { "usuario_id", "postagem_id" }))
public class Curtida {

    /** Identificador único da curtida. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuário que realizou a curtida. */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Postagem curtida. */
    @ManyToOne
    @JoinColumn(name = "postagem_id", nullable = false)
    private Postagem postagem;
}
