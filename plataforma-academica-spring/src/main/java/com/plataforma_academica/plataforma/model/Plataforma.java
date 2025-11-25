package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "plataforma")
public class Plataforma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; // opcional

    @OneToMany(mappedBy = "plataforma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "plataforma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Postagem> postagens;
}
