package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "curtida", uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "postagem_id"}))
public class Curtida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "postagem_id", nullable = false)
    private Postagem postagem;
}
