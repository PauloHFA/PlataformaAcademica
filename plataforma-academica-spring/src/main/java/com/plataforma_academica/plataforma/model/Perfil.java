package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade JPA que representa um Perfil estendido de usuário.
 *
 * Camada: Persistence / Domain Entity (Identity Context)
 * Contexto de Negócio: Perfil acadêmico com bio, foto, curso e dados
 * complementares, herdando de Usuario via herança JOINED.
 *
 * @see Usuario
 * @see REQ-002 (Perfil Acadêmico)
 */
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