package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@Table(name = "submissaoatividade")
public class SubmissaoAtividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com a Atividade que está sendo submetida
    // @ManyToOne: Muitas submissões para uma única Atividade
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atividade_id", nullable = false)
    private Atividade atividade;

    // Relacionamento com o Usuário que fez a submissão (o aluno)
    // @ManyToOne: Muitos alunos podem fazer submissões
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    // O caminho ou URL do arquivo/documento submetido
    private String urlDocumento;

    // Descrição da submissão
    @Column(columnDefinition = "TEXT")
    private String descricao;

    // Indica a data e hora exata da submissão
    private LocalDateTime dataSubmissao;

    // Campo opcional para a nota atribuída
    private Double nota;

    // Campo opcional para feedback do professor
    @Column(columnDefinition = "TEXT")
    private String feedback;

    private LocalDateTime dataCorrecao;

    private Boolean recebida = false;

    private LocalDateTime dataRecebimento;

}