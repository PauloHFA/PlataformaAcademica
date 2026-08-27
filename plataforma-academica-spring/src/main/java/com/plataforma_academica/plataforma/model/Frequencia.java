package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Entidade JPA que representa o registro de frequência escolar/acadêmica de um
 * aluno.
 * 
 * Camada: Persistence / Domain Entity (Academic Context)
 * Relaciona aluno, sala de aula, data e status de presença.
 * 
 * @see docs/domain/academic_context.md
 * @see REQ-025 (Controle de Frequência em Salas de Aula)
 */
@Entity
@Data
@Table(name = "frequencia")
public class Frequencia {

    /** Identificador único do registro de frequência. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Aluno cuja frequência está sendo registrada. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    /** Sala de aula onde a chamada/frequencia ocorreu. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private SaladeAula salaDeAula;

    /** Data da aula/frequência. */
    @Column(nullable = false)
    private LocalDate data;

    /** Indicador se o aluno esteve presente (true) ou ausente (false). */
    @Column(nullable = false)
    private Boolean presente;

    /** Justificativa opcional para ausências (texto livre). */
    @Column(columnDefinition = "TEXT")
    private String justificativa;
}
