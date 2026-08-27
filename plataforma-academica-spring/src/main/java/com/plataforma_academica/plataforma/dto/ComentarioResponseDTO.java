package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de resposta para Comentários.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Comentario
 * @see ComentarioMapper
 */
@Data
public class ComentarioResponseDTO {

    private Long id;
    private String conteudo;
    private LocalDateTime dataCriacao;

    // Informações do autor
    private Long autorId;
    private String autorNome;

    // Destino do comentário
    private String tipoDestino; // EX: "POSTAGEM", "ATIVIDADE", "SALA"
    private Long destinoId; // Pode ser postagemId, atividadeId ou salaId
}
