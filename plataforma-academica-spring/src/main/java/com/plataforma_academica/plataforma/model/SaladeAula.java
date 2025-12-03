package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Data
@Getter
@Setter
@Table(name = "sala_de_aula")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SaladeAula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; // Nome da sala de aula (e.g., "POO 2024.1")
    
    @Column(unique = true, nullable = false, length = 8)
    private String codigoSala; // Código único para compartilhar (ex: "A7X9K2M5")

    // 1. Criador da Sala (Relação Many-to-One)
    // Muitas salas de aula são criadas por um único usuário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = false)
    private Usuario criador;

    // 2. Membros da Sala (Relação Many-to-Many)
    // Usamos uma tabela de junção para mapear a relação de que um usuário pode estar em várias salas.
    @ManyToMany
    @JoinTable(
            name = "sala_membros", // Nome da tabela de junção
            joinColumns = @JoinColumn(name = "sala_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
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