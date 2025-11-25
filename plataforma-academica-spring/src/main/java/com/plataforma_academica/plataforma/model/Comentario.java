package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Entity
@Table(name = "comentarios")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false) // O autor é obrigatório
    private Usuario autor;

    // Relacionamento com Postagem: Opcional (nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postagem_id", nullable = true)
    private Postagem postagem;

    // NOVO: Relacionamento com Atividade: Opcional (nullable = true)
    // Isso permite comentários em uma atividade, que pode ser o "comentário da turma" para aquele tópico.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atividade_id", nullable = true)
    @JsonBackReference("comentarios-atividade")
    private Atividade atividade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_de_aula_id", nullable = true)
    @JsonBackReference("comentarios-sala")
    private SaladeAula saladeAula;


    // NOVO: Campo para indicar o tipo de destino (útil para consultas e validação)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDestinoComentario tipoDestino;

    @Lob // Para permitir comentários mais longos
    private String conteudo;

    private LocalDateTime dataCriacao;

    // IMPORTANTE:
    // Você deve implementar uma lógica de validação antes de persistir
    // para garantir que APENAS UM dos campos (postagem ou atividade) não seja nulo.
}