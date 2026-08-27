package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma conexão de amizade entre dois usuários.
 * 
 * Camada: Persistence / Domain Entity
 * 
 * Define o ciclo de vida de uma solicitação de amizade:
 * 
 * <pre>
 * PENDENTE -> ACEITO   (destinatário aceita)
 * PENDENTE -> RECUSADO (destinatário recusa)
 * </pre>
 * 
 * Invariantes:
 * - Não pode haver amizade de um usuário consigo mesmo.
 * - A combinação (solicitante, destinatário) é única.
 * - Status inicial sempre é PENDENTE.
 */
@Data
@Getter
@Setter
@Entity
@Table(name = "amizades", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "solicitante_id", "destinatario_id" })
})
public class Amizade {

    /** Identificador único da amizade (chave primária auto-incrementada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuário que enviou a solicitação de amizade. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Usuario solicitante;

    /** Usuário que recebeu a solicitação de amizade. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;

    /** Status atual da amizade (PENDENTE, ACEITO, RECUSADO). */
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDENTE;

    /** Data e hora de criação da amizade. */
    private LocalDateTime criadoEm = LocalDateTime.now();

    /**
     * Enumeração dos possíveis status de uma amizade.
     */
    public enum Status {
        PENDENTE, ACEITO, RECUSADO
    }
}
