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
@Table(name = "amizades", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"solicitante_id","destinatario_id"})
})
public class Amizade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuário que enviou a solicitação
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Usuario solicitante;

    // Usuário que recebeu a solicitação
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDENTE;

    private LocalDateTime criadoEm = LocalDateTime.now();

    public enum Status { PENDENTE, ACEITO, RECUSADO }
}
