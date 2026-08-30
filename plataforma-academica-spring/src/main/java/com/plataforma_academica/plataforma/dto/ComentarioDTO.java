package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de transferÃªncia para criaÃ§Ã£o/atualizaÃ§Ã£o de ComentÃ¡rios.
 * 
 * Camada: Presentation / DTO
 * Contexto de NegÃ³cio: Transporta dados de comentÃ¡rios entre Controller e
 * Service.
 * 
 * @see Comentario
 * @see ComentarioMapper
 */
@Data
public class ComentarioDTO {

    private UUID id;
    private UUID autorId;

    private UUID postagemId; // pode ser null
    private UUID atividadeId; // pode ser null
    private UUID salaId; // pode ser null

    private String tipoDestino; // enum em String

    private String conteudo;
    private LocalDateTime dataCriacao;
}

