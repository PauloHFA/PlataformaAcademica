package com.plataforma_academica.plataforma.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence.SalaMembroEntity;
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
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    /** Nome da sala (ex: "POO 2024.1"). */
    private String nome;

    /** Código único para compartilhamento (ex: "A7X9K2M5"). */
    @Column(unique = true, nullable = false, length = 8)
    private String codigoSala;

    /** Código da sala (ex: "MAT-101"). */
    @Column(nullable = false)
    private String codigo;

    /** Criador (professor) da sala. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = false)
    private Usuario criador;

    // 2. Membros da Sala (Relação Many-to-Many)
    // Usamos uma tabela de junção para mapear a relação de que um usuário pode
    // estar em várias salas.
    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalaMembroEntity> membros; // Lista de membros/alunos

    @Transient
    private List<Usuario> usuarios;

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<UUID> getUsuarioIds() {
        return membros.stream().map(SalaMembroEntity::getUsuarioId).collect(java.util.stream.Collectors.toList());
    }

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

    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}