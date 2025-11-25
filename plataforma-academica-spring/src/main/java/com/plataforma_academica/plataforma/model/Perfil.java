package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "perfis")
@Data
@Getter
@Setter
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bio;
    private String fotoPerfil;
    private String curso;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}