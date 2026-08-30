package com.plataforma_academica.plataforma.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade JPA que representa conteúdo comercializado no mercado acadêmico.
 * 
 * Camada: Persistence / Domain Entity (Academic / Commercial Context)
 * Contexto de Negócio: Material didático, cursos, templates e exercícios
 * vendidos ou distribuídos dentro da plataforma, com controle de preço,
 * avaliações e downloads.
 * 
 * @see docs/domain/academic_context.md
 * @see REQ-050 (Mercado de Conteúdo Acadêmico)
 */
@Entity
@Table(name = "conteudo_mercado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConteudoMercado {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 1000)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConteudo tipoConteudo;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(name = "caminho_arquivo")
    private String caminhoArquivo;

    @Column(name = "url_download")
    private String urlDownload;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(nullable = false)
    private Integer downloads = 0;

    @Column(nullable = false)
    private Double avaliacao = 0.0;

    @Column(name = "total_avaliacoes")
    private Integer totalAvaliacoes = 0;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @Column(length = 500)
    private String tags;

    @Column(name = "nivel_dificuldade")
    @Enumerated(EnumType.STRING)
    private NivelDificuldade nivelDificuldade;

    @Column(name = "categoria")
    private String categoria;

    public enum TipoConteudo {
        MATERIAL_DIDATICO,
        CURSO_VIDEO,
        APOSTILA,
        EXERCICIO_RESOLVIDO,
        TEMPLATE_PROJETO,
        GUIA_ESTUDO
    }

    public enum NivelDificuldade {
        INICIANTE,
        INTERMEDIARIO,
        AVANCADO
    }


    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}