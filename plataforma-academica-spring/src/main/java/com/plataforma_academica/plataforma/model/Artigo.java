package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidade JPA que representa um Artigo publicado na plataforma.
 *
 * Camada: Persistence / JPA Entity (Academic Context)
 * Contexto de Negócio: Publicação de conteúdo acadêmico longo (artigos,
 * pesquisas, ensaios) vinculados a um autor e rastreáveis por data.
 * Padrões aplicados: Rich Domain Model (entidade raiz), Repository Pattern.
 *
 * @see docs/domain/academic_context.md
 * @see REQ-010 (Publicação de Artigos Acadêmicos)
 */
@Entity
@Data
@Table(name = "artigo")
public class Artigo {

    /** Identificador único do artigo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título do artigo (exposto publicamente no feed e indexado para busca).
     */
    private String titulo;

    /**
     * Conteúdo completo do artigo (suporta textos longos via Lob).
     * Trade-off: Lob permite artigos extensos sem limite rígido de VARCHAR,
     * mas pode impactar performance de leitura em listas grandes.
     */
    @Lob
    private String conteudo;

    /**
     * Usuário autor responsável pela criação do artigo.
     * 
     * @see Usuario
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    /**
     * Data e hora de criação do registro (populada automaticamente).
     * Imutável após persistência.
     */
    private LocalDateTime criadoEm = LocalDateTime.now();

    /**
     * Data e hora da última atualização do artigo (nula até primeira atualização).
     * Usada para cache busting e ordenação cronológica.
     */
    private LocalDateTime atualizadoEm;
}
