package com.plataforma_academica.plataforma.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa a associação de um usuário a uma comunidade.
 * 
 * Camada: Persistence / Domain Entity (Social Context)
 * Contexto de Negócio: Gerencia a relação Many-to-Many entre {@link Usuario} e
 * {@link Comunidade}
 * com atributos extras como papel (ADMIN, MOD, MEMBRO) e data de ingresso.
 * 
 * Invariantes:
 * - A combinação (usuario_id, comunidade_id) é única.
 * 
 * @see Comunidade
 * @see Usuario
 * @see docs/domain/social_context.md
 * @see REQ-016 (Gestão de Membros em Comunidades)
 */
@Data
@Getter
@Setter
@Entity
@Table(name = "membros_comunidade", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "usuario_id", "comunidade_id" })
})
public class MembroComunidade {
    /** Identificador único da associação. */
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    /** Usuário membro da comunidade. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /** Comunidade à qual o usuário pertence. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "comunidade_id")
    private Comunidade comunidade;

    /** Papel do usuário na comunidade (ex: ADMIN, MOD, MEMBRO). */
    private String papel = "MEMBRO";

    /** Data e hora de ingresso na comunidade. */
    private LocalDateTime entrouEm = LocalDateTime.now();



    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}