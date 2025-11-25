package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Entity
@Table(name = "membros_comunidade", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "comunidade_id"})
})
public class MembroComunidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "comunidade_id")
    private Comunidade comunidade;

    private String papel = "MEMBRO"; // ex: ADMIN, MOD, MEMBRO
    private LocalDateTime entrouEm = LocalDateTime.now();

}