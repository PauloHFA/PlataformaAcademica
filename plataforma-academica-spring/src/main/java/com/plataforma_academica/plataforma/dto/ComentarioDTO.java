package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComentarioDTO {

    private Long id;
    private Long autorId;

    private Long postagemId;     // pode ser null
    private Long atividadeId;    // pode ser null
    private Long salaId;         // pode ser null

    private String tipoDestino;  // enum em String

    private String conteudo;
    private LocalDateTime dataCriacao;
}
