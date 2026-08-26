package com.plataforma_academica.plataforma.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidade JPA que representa uma Atividade acadêmica dentro de uma sala de
 * aula.
 *
 * Camada: Persistence / JPA Entity (Academic Context)
 * Contexto de Negócio: Tarefas, exercícios e entregáveis criados por
 * professores
 * dentro de uma sala de aula, com data limite, pontuação e submissão vinculada.
 * Padrões aplicados: Aggregate Root (Atividade), Repository Pattern,
 * One-to-Many com Comentario (via JsonManagedReference).
 *
 * Invariantes:
 * - Autor (professor) é obrigatório (nullable = false).
 * - Sala de aula é obrigatória (nullable = false).
 * - Data de entrega deve ser futura no momento da criação (regra de negócio).
 *
 * @see docs/domain/academic_context.md
 * @see REQ-020 (Criação de Atividades)
 */
@Entity
@Data
@Getter
@Setter
@Table(name = "atividade")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Atividade {
    /** Identificador único da atividade. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Título da atividade. */
    private String titulo;

    /** Descrição detalhada da atividade. */
    private String descricao;

    /** Tipo de documento esperado para submissão (PDF, Link, ZIP, etc.). */
    private String tipoDocumentoSubmissao;

    /** Data limite para entrega da atividade. */
    private LocalDate dataEntrega;

    /** Pontuação máxima atribuída à atividade. */
    private Double pontos;

    /** URL do documento anexado pelo professor. */
    private String documentoUrl;

    /**
     * Comentários associados à atividade (feed de discussão).
     * Cascade ALL garante que comentários sejam removidos com a atividade.
     */
    @OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("comentarios-atividade")
    private List<Comentario> comentarios;

    /**
     * Autor da atividade (tipicamente um professor).
     * 
     * @see Usuario
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    /**
     * Sala de aula à qual a atividade pertence.
     * 
     * @see SaladeAula
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    @JsonBackReference("atividades")
    private SaladeAula salaDeAula;

    // Submissões de alunos são persistidas via SubmissaoAtividade
    // (entidade separada para evitar acoplamento direto aqui).
}