package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entidade JPA que representa uma sala de aula (turma) dentro da plataforma.
 *
 * Camada: Persistence / Domain Entity (Academic Context)
 * Contexto de Negócio: Espaço de ensino onde professores criam atividades,
 * alunos participam e comentários são publicados no feed.
 * Padrões aplicados: Aggregate Root, Repository Pattern, Many-to-Many
 * (membros).
 *
 * @see docs/domain/academic_context.md
 * @see REQ-018 (Criação de Salas de Aula)
 */
@Entity
@Data
@Getter
@Setter
@Table(name = "sala_de_aula")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class SaladeAula {
    /** Identificador único da sala. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome da sala (ex: "POO 2024.1"). */
    private String nome;

    /** Código único para compartilhamento (ex: "A7X9K2M5"). */
    @Column(unique = true, nullable = false, length = 8)
    private String codigoSala;

    /** Criador (professor) da sala. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = false)
    private Usuario criador;

    // 2. Membros da Sala (Relação Many-to-Many)
    // Usamos uma tabela de junção para mapear a relação de que um usuário pode
    // estar em várias salas.
    @ManyToMany
    @JoinTable(name = "sala_membros", // Nome da tabela de junção
            joinColumns = @JoinColumn(name = "sala_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> usuarios; // Lista de membros/alunos

    // 3. Atividades da Sala (Relação One-to-Many)
    // Uma sala tem muitas atividades
    @OneToMany(mappedBy = "salaDeAula", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("atividades")
    private List<Atividade> atividades;

    // 4. Comentários da Sala (Relação One-to-Many Generalizada)
    // Comentários feitos diretamente no feed/timeline da sala de aula.
    @OneToMany(mappedBy = "saladeAula", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("comentarios-sala")
    private List<Comentario> comentarios;
}