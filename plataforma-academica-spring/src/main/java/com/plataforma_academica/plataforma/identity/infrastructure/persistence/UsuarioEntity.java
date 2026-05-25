package com.plataforma_academica.plataforma.identity.infrastructure.persistence;

import com.plataforma_academica.plataforma.identity.domain.model.Papel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entidade JPA para persistência do agregado Usuario.
 * Separada do modelo de domínio para isolamento (Clean Architecture).
 */
@Entity
@Table(name = "usuario")
@Getter
@Setter
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "biografia", columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "instituicao")
    private String instituicao;

    @Column(name = "cep")
    private String cep;

    @Column(name = "site")
    private String site;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_papeis", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "papel")
    @Enumerated(EnumType.STRING)
    private Set<Papel> papeis = new HashSet<>();

    @Column(name = "pontos", precision = 19, scale = 2)
    private BigDecimal pontos = BigDecimal.ZERO;

    @Column(name = "moedas", precision = 19, scale = 2)
    private BigDecimal moedas = BigDecimal.ZERO;

    @Column(name = "nivel")
    private String nivel = "Bronze";

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;
}