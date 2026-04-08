package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "interacao_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteracaoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "tipo_interacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoInteracao tipoInteracao;

    @Column(name = "entidade_tipo")
    private String entidadeTipo; // "POSTAGEM", "ATIVIDADE", "COMUNIDADE", etc.

    @Column(name = "entidade_id")
    private Long entidadeId;

    @Column(name = "peso_interacao", nullable = false)
    private Double pesoInteracao = 1.0;

    @Column(name = "data_interacao")
    private LocalDateTime dataInteracao = LocalDateTime.now();

    @Column(name = "tags")
    private String tags; // JSON array de tags relacionadas

    public enum TipoInteracao {
        VISUALIZACAO,
        CURTIDA,
        COMENTARIO,
        COMPARTILHAMENTO,
        PARTICIPACAO_ATIVIDADE,
        ENTRADA_COMUNIDADE,
        ENVIO_SOLICITACAO_AMIZADE,
        ACEITACAO_AMIZADE
    }
}