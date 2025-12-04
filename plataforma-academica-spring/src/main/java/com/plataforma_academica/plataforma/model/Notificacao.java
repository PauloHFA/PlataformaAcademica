package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notificacao")
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private String mensagem;
    private String tipo; // ATIVIDADE_CRIADA, NOTA_ATRIBUIDA, etc
    private Long referenciaId; // ID da atividade/sala relacionada
    private Boolean lida = false;
    private LocalDateTime dataCriacao = LocalDateTime.now();
}
