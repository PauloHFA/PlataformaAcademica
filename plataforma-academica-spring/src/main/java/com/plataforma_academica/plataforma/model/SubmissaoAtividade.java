package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa a submissão de uma atividade acadêmica por um
 * aluno.
 * 
 * Camada: Persistence / Domain Entity (Academic Context)
 * Contexto de Negócio: Armazena o documento enviado pelo aluno, notas
 * atribuídas,
 * feedback do professor e controle de recebimento/correção.
 * Padrões aplicados: Repository Pattern, Relacionamento ManyToOne com Atividade
 * e Usuario.
 * 
 * @see Atividade
 * @see Usuario
 * @see docs/domain/academic_context.md
 * @see REQ-020 (Submissão e Avaliação de Atividades)
 */
@Entity
@Data
@Getter
@Setter
@Table(name = "submissaoatividade")
public class SubmissaoAtividade {
    /** Identificador único da submissão. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Atividade acadêmica associada à submissão. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atividade_id", nullable = false)
    private Atividade atividade;

    /** Aluno responsável pela entrega da submissão. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    /** URL ou caminho físico do documento/arquivo submetido. */
    private String urlDocumento;

    /** Descrição ou observações enviadas pelo aluno. */
    @Column(columnDefinition = "TEXT")
    private String descricao;

    /** Data e hora em que a submissão foi realizada. */
    private LocalDateTime dataSubmissao;

    /** Nota numérica atribuída pelo professor (opcional). */
    private Double nota;

    /** Feedback textual fornecido pelo professor (opcional). */
    @Column(columnDefinition = "TEXT")
    private String feedback;

    /** Data e hora da correção/avaliação pelo professor. */
    private LocalDateTime dataCorrecao;

    /** Flag indicando se a submissão foi recebida pelo sistema/professor. */
    private Boolean recebida = false;

    /** Data e hora do recebimento confirmado. */
    private LocalDateTime dataRecebimento;

}