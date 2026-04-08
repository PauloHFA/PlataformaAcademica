package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "frequencia")
public class Frequencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private SaladeAula salaDeAula;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private Boolean presente;

    @Column(columnDefinition = "TEXT")
    private String justificativa;
}
