package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "recomendacao_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacaoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_recomendado_id", nullable = false)
    private Usuario usuarioRecomendado;

    @Column(name = "score_similaridade", nullable = false)
    private Double scoreSimilaridade;

    @Column(name = "motivo_recomendacao")
    private String motivoRecomendacao;

    @Column(name = "tipo_recomendacao")
    @Enumerated(EnumType.STRING)
    private TipoRecomendacao tipoRecomendacao;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "ativo")
    private Boolean ativo = true;

    public enum TipoRecomendacao {
        ESTUDO_GRUPO,
        AMIZADE,
        MENTORIA,
        COLABORACAO_PROJETO
    }
}