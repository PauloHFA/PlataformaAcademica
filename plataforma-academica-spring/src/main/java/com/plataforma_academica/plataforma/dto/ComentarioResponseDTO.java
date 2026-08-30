package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de resposta para ComentÃ¡rios.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Comentario
 * @see ComentarioMapper
 */
@Data
public class ComentarioResponseDTO {

    private UUID id;
    private String conteudo;
    private LocalDateTime dataCriacao;

    // InformaÃ§Ãµes do autor
    private UUID autorId;
    private String autorNome;

    // Destino do comentÃ¡rio
    private String tipoDestino; // EX: "POSTAGEM", "ATIVIDADE", "SALA"
    private UUID destinoId; // Pode ser postagemId, atividadeId ou salaId
}

