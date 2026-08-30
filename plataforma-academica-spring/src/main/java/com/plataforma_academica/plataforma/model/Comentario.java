package com.plataforma_academica.plataforma.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa um Comentário realizado por um usuário.
 * 
 * Camada: Persistence / JPA Entity
 * Pode ser vinculado a diferentes tipos de destino (postagem, atividade ou sala
 * de aula),
 * gerenciado pelo campo discriminador {@link TipoDestinoComentario}.
 */
@Data
@Getter
@Setter
@Entity
@Table(name = "comentarios")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Comentario {
    /** Identificador único do comentário. */
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    /** Usuário autor do comentário. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    /** Postagem comentada (opcional). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postagem_id", nullable = true)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Postagem postagem;

    /** Atividade comentada (opcional). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atividade_id", nullable = true)
    @JsonBackReference("comentarios-atividade")
    private Atividade atividade;

    /** Sala de aula onde o comentário foi publicado (opcional). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_de_aula_id", nullable = true)
    @JsonBackReference("comentarios-sala")
    private SaladeAula saladeAula;

    /** Tipo de destino do comentário (POSTAGEM, ATIVIDADE, SALA). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDestinoComentario tipoDestino;

    @Lob
    private String conteudo;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // IMPORTANTE:
    // Você deve implementar uma lógica de validação antes de persistir
    // para garantir que APENAS UM dos campos (postagem ou atividade) não seja nulo.


    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}