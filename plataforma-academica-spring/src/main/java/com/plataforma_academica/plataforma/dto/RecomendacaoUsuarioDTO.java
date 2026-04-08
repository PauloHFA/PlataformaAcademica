package com.plataforma_academica.plataforma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacaoUsuarioDTO {

    private Long id;
    private UsuarioDTO usuario;
    private UsuarioDTO usuarioRecomendado;
    private Double scoreSimilaridade;
    private String motivoRecomendacao;
    private String tipoRecomendacao;
    private LocalDateTime dataCriacao;
    private Boolean ativo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioDTO {
        private Long id;
        private String nome;
        private String email;
        private String fotoPerfil;
        private String curso;
        private String interesses;
    }
}