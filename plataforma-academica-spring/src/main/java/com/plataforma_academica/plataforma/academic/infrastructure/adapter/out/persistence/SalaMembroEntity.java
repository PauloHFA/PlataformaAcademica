package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sala_membro", uniqueConstraints = {
        @UniqueConstraint(name = "uk_sala_usuario", columnNames = { "sala_id", "usuario_id" }) })
@Getter
@Setter
public class SalaMembroEntity {
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private SaladeAulaEntity sala;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false)
    private PapelMembro papel;

    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime dataEntrada;

    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public enum PapelMembro {
        DOCENTE, ALUNO
    }
}