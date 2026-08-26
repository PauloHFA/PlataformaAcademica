package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de transferência para criação/atualização de Comentários.
 * 
 * Camada: Presentation / DTO
 * Contexto de Negócio: Transporta dados de comentários entre Controller e
 * Service.
 * 
 * @see Comentario
 * @see ComentarioMapper
 */
@Data
public class ComentarioDTO {

    private Long id;
    private Long autorId;

    private Long postagemId; // pode ser null
    private Long atividadeId; // pode ser null
    private Long salaId; // pode ser null

    private String tipoDestino; // enum em String

    private String conteudo;
    private LocalDateTime dataCriacao;
}
