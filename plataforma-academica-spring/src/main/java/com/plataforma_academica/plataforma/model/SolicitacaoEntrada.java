package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma solicitação de entrada de um usuário em uma
 * sala de aula.
 * 
 * Camada: Persistence / Domain Entity (Academic Context)
 * Contexto de Negócio: Controle de acesso a salas de aula restritas, onde o
 * usuário solicita
 * ingresso e o professor/criador aprova ou rejeita.
 * 
 * @see SaladeAula
 * @see Usuario
 * @see docs/domain/academic_context.md
 * @see REQ-025 (Solicitação e Aprovação de Entrada em Salas)
 */
@Data
@Entity
@Table(name = "solicitacao_entrada")
public class SolicitacaoEntrada {

    /** Identificador único da solicitação. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Sala de aula alvo da solicitação. */
    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private SaladeAula sala;

    /** Usuário solicitante (aluno). */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Status atual da solicitação (PENDENTE, APROVADA, REJEITADA). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status = StatusSolicitacao.PENDENTE;

    /** Data e hora em que a solicitação foi enviada. */
    @Column(nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    /** Data e hora em que a solicitação foi respondida. */
    private LocalDateTime dataResposta;

    /**
     * Enumeração dos possíveis status de uma solicitação de entrada.
     */
    public enum StatusSolicitacao {
        PENDENTE, APROVADA, REJEITADA
    }
}
