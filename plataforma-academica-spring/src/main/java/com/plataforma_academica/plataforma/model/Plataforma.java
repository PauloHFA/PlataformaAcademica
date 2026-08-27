package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Entidade JPA que representa o escopo global de Plataforma.
 * 
 * Camada: Persistence / Domain Entity
 * Agrupa usuários e postagens sob uma mesma instância de plataforma.
 * 
 * @see docs/architecture/context-map.md
 */
@Data
@Entity
@Getter
@Setter
@Table(name = "plataforma")
public class Plataforma {
    /** Identificador único da plataforma. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome descritivo da instância da plataforma. */
    private String nome; // opcional

    /** Lista de usuários vinculados a esta plataforma. */
    @OneToMany(mappedBy = "plataforma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usuario> usuarios;

    /** Lista de postagens associadas a esta plataforma. */
    @OneToMany(mappedBy = "plataforma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Postagem> postagens;
}
