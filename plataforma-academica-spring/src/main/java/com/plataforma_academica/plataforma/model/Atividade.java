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

@Entity
@Data
@Getter
@Setter
@Table(name = "atividade")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    // Descrição detalhada da atividade
    private String descricao;

    // Define o tipo de documento que o aluno deve submeter (e.g., "PDF", "Link", "ZIP")
    private String tipoDocumentoSubmissao;

    // Data limite para a entrega da atividade
    private LocalDate dataEntrega;

    private Double pontos;

    @OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("comentarios-atividade")
    private List<Comentario> comentarios;

    // Relacionamento com o usuário que postou a atividade
    // @ManyToOne: Muitas atividades podem ser postadas por um único usuário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false) // Coluna de chave estrangeira que aponta para Usuario
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    @JsonBackReference("atividades")
    private SaladeAula salaDeAula;

    //existe outro modelo para realizar as submissoes de atividades chamado de SubmissaoAtividade
}