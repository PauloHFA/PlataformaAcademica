package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "perfis")
@DiscriminatorValue("PERFIL")
@Data
@Getter
@Setter
public class Perfil extends Usuario {

    @Lob
    @Column(columnDefinition = "TEXT")
    private String bio;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String fotoPerfil;
    @Column(length = 300)
    private String curso;
}