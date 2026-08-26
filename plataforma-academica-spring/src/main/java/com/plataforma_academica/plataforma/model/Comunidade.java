package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade JPA que representa uma comunidade de interesse dentro da plataforma.
 *
 * Camada: Persistence / Domain Entity (Social Context)
 * Contexto de Negócio: Grupos temáticos (acadêmicos, de estudo, de interesse)
 * onde usuários se organizam, compartilham conteúdo e interagem.
 * Padrões aplicados: Aggregate Root (Comunidade), Repository Pattern,
 * Many-to-Many via tabela de junção (MembroComunidade).
 *
 * Invariantes:
 * - Nome deve ser único dentro da plataforma (restrição de negócio).
 * - Criador (dono) não pode ser removido sem transferência de propriedade.
 *
 * @see docs/domain/social_context.md
 * @see REQ-015 (Criação de Comunidades)
 */
@Data
@Getter
@Setter
@Entity
@Table(name = "comunidades")
public class Comunidade {
    /** Identificador único da comunidade (chave primária auto-incrementada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome da comunidade (exposto publicamente, deve ser único). */
    private String nome;

    /** Descrição resumida do propósito e tema da comunidade. */
    private String descricao;

    /** Data e hora de criação da comunidade. */
    private LocalDateTime criadoEm = LocalDateTime.now();

    /**
     * Usuário criador (dono) da comunidade.
     * 
     * @see Usuario
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dono_id")
    private Usuario dono;

    // Membros são persistidos via entidade MembroComunidade (tabela de junção)
    // para evitar explosão de coleções em comunidades grandes.
    // Trade-off: consulta de membros requer join adicional, mas mantém
    // a entidade raiz leve para operações de feed e busca.
}
